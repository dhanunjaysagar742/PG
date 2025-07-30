// Device Entity
@Entity
@Table(name = "network_devices")
public class NetworkDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ip_address", unique = true)
    private String ipAddress;
    
    @Column(name = "mac_address")
    private String macAddress;
    
    @Column(name = "hostname")
    private String hostname;
    
    @Column(name = "vendor")
    private String vendor;
    
    @Column(name = "device_type")
    private String deviceType;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DeviceStatus status;
    
    @Column(name = "first_seen")
    private LocalDateTime firstSeen;
    
    @Column(name = "last_seen")
    private LocalDateTime lastSeen;
    
    @Column(name = "is_authorized")
    private Boolean isAuthorized = false;
    
    // Constructors, getters, setters
    public NetworkDevice() {}
    
    public NetworkDevice(String ipAddress, String macAddress) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.firstSeen = LocalDateTime.now();
        this.lastSeen = LocalDateTime.now();
        this.status = DeviceStatus.ONLINE;
    }
    
    // Standard getters and setters...
}

enum DeviceStatus {
    ONLINE, OFFLINE, UNKNOWN
}

// Repository
@Repository
public interface NetworkDeviceRepository extends JpaRepository<NetworkDevice, Long> {
    Optional<NetworkDevice> findByIpAddress(String ipAddress);
    Optional<NetworkDevice> findByMacAddress(String macAddress);
    List<NetworkDevice> findByStatus(DeviceStatus status);
    List<NetworkDevice> findByIsAuthorized(Boolean isAuthorized);
    List<NetworkDevice> findByLastSeenBefore(LocalDateTime dateTime);
}

// ARP Scanner Service
@Service
@Slf4j
public class ArpScannerService {
    
    @Autowired
    private NetworkDeviceRepository deviceRepository;
    
    @Value("${network.subnet:192.168.1.0/24}")
    private String networkSubnet;
    
    public List<NetworkDevice> scanNetwork() {
        log.info("Starting network scan for subnet: {}", networkSubnet);
        List<NetworkDevice> discoveredDevices = new ArrayList<>();
        
        try {
            // Parse ARP table
            List<ArpEntry> arpEntries = parseArpTable();
            
            // Ping sweep for active devices
            List<String> activeIps = performPingSweep();
            
            // Combine results
            for (ArpEntry entry : arpEntries) {
                NetworkDevice device = processArpEntry(entry);
                if (activeIps.contains(entry.getIpAddress())) {
                    device.setStatus(DeviceStatus.ONLINE);
                    device.setLastSeen(LocalDateTime.now());
                }
                discoveredDevices.add(device);
            }
            
            // Save to database
            saveDiscoveredDevices(discoveredDevices);
            
        } catch (Exception e) {
            log.error("Error during network scan", e);
        }
        
        return discoveredDevices;
    }
    
    private List<ArpEntry> parseArpTable() throws IOException {
        List<ArpEntry> entries = new ArrayList<>();
        
        // Execute arp command
        Process process = Runtime.getRuntime().exec("arp -a");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        String line;
        while ((line = reader.readLine()) != null) {
            ArpEntry entry = parseArpLine(line);
            if (entry != null) {
                entries.add(entry);
            }
        }
        
        return entries;
    }
    
    private ArpEntry parseArpLine(String line) {
        // Parse ARP table line: "hostname (192.168.1.100) at aa:bb:cc:dd:ee:ff [ether] on eth0"
        Pattern pattern = Pattern.compile("\\((\\d+\\.\\d+\\.\\d+\\.\\d+)\\)\\s+at\\s+([a-fA-F0-9:]{17})");
        Matcher matcher = pattern.matcher(line);
        
        if (matcher.find()) {
            String ip = matcher.group(1);
            String mac = matcher.group(2).toLowerCase();
            return new ArpEntry(ip, mac);
        }
        
        return null;
    }
    
    private List<String> performPingSweep() {
        List<String> activeIps = new ArrayList<>();
        String[] subnetParts = networkSubnet.split("/");
        String baseIp = subnetParts[0].substring(0, subnetParts[0].lastIndexOf('.'));
        
        // Ping sweep (1-254)
        for (int i = 1; i <= 254; i++) {
            String ip = baseIp + "." + i;
            if (pingHost(ip)) {
                activeIps.add(ip);
            }
        }
        
        return activeIps;
    }
    
    private boolean pingHost(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(1000); // 1 second timeout
        } catch (IOException e) {
            return false;
        }
    }
    
    private NetworkDevice processArpEntry(ArpEntry entry) {
        Optional<NetworkDevice> existing = deviceRepository.findByIpAddress(entry.getIpAddress());
        
        NetworkDevice device;
        if (existing.isPresent()) {
            device = existing.get();
            device.setLastSeen(LocalDateTime.now());
        } else {
            device = new NetworkDevice(entry.getIpAddress(), entry.getMacAddress());
            device.setVendor(lookupMacVendor(entry.getMacAddress()));
            device.setHostname(resolveHostname(entry.getIpAddress()));
        }
        
        return device;
    }
    
    private String lookupMacVendor(String macAddress) {
        // Simple MAC vendor lookup - in production, use OUI database
        String oui = macAddress.substring(0, 8).replace(":", "").toUpperCase();
        Map<String, String> vendors = Map.of(
            "00:50:56", "VMware",
            "08:00:27", "VirtualBox",
            "00:0C:29", "VMware",
            "00:15:5D", "Microsoft"
        );
        return vendors.getOrDefault(oui, "Unknown");
    }
    
    private String resolveHostname(String ipAddress) {
        try {
            InetAddress addr = InetAddress.getByName(ipAddress);
            return addr.getHostName();
        } catch (Exception e) {
            return null;
        }
    }
    
    private void saveDiscoveredDevices(List<NetworkDevice> devices) {
        for (NetworkDevice device : devices) {
            deviceRepository.save(device);
        }
    }
}

// ARP Entry helper class
class ArpEntry {
    private String ipAddress;
    private String macAddress;
    
    public ArpEntry(String ipAddress, String macAddress) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
    }
    
    // Getters
    public String getIpAddress() { return ipAddress; }
    public String getMacAddress() { return macAddress; }
}

// REST Controller
@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*")
public class NetworkDeviceController {
    
    @Autowired
    private ArpScannerService scannerService;
    
    @Autowired
    private NetworkDeviceRepository deviceRepository;
    
    @GetMapping
    public ResponseEntity<List<NetworkDevice>> getAllDevices() {
        List<NetworkDevice> devices = deviceRepository.findAll();
        return ResponseEntity.ok(devices);
    }
    
    @GetMapping("/unauthorized")
    public ResponseEntity<List<NetworkDevice>> getUnauthorizedDevices() {
        List<NetworkDevice> devices = deviceRepository.findByIsAuthorized(false);
        return ResponseEntity.ok(devices);
    }
    
    @PostMapping("/scan")
    public ResponseEntity<List<NetworkDevice>> performScan() {
        List<NetworkDevice> devices = scannerService.scanNetwork();
        return ResponseEntity.ok(devices);
    }
    
    @PutMapping("/{id}/authorize")
    public ResponseEntity<NetworkDevice> authorizeDevice(@PathVariable Long id) {
        Optional<NetworkDevice> device = deviceRepository.findById(id);
        if (device.isPresent()) {
            NetworkDevice dev = device.get();
            dev.setIsAuthorized(true);
            deviceRepository.save(dev);
            return ResponseEntity.ok(dev);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getNetworkStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDevices", deviceRepository.count());
        stats.put("onlineDevices", deviceRepository.findByStatus(DeviceStatus.ONLINE).size());
        stats.put("unauthorizedDevices", deviceRepository.findByIsAuthorized(false).size());
        return ResponseEntity.ok(stats);
    }
}

// Scheduled scanning
@Component
public class ScheduledNetworkScanner {
    
    @Autowired
    private ArpScannerService scannerService;
    
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void scheduledScan() {
        log.info("Running scheduled network scan");
        scannerService.scanNetwork();
    }
}