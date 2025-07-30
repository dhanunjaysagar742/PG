// ===========================================
// BACKEND CODE - Spring Boot Application
// ===========================================

// File: src/main/java/com/networkdiscovery/NetworkDiscoveryApplication.java
package com.networkdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NetworkDiscoveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(NetworkDiscoveryApplication.class, args);
    }
}

// File: src/main/java/com/networkdiscovery/entity/AuthorizedDevice.java
package com.networkdiscovery.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "authorized_devices")
public class AuthorizedDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ip_address", unique = true)
    private String ipAddress;
    
    @Column(name = "mac_address", unique = true)
    private String macAddress;
    
    @Column(name = "device_name")
    private String deviceName;
    
    @Column(name = "device_type")
    private String deviceType;
    
    @Column(name = "owner")
    private String owner;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "added_by")
    private String addedBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    // Constructors
    public AuthorizedDevice() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public AuthorizedDevice(String ipAddress, String macAddress, String deviceName) {
        this();
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.deviceName = deviceName;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAddedBy() { return addedBy; }
    public void setAddedBy(String addedBy) { this.addedBy = addedBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

// File: src/main/java/com/networkdiscovery/entity/UnauthorizedDevice.java
package com.networkdiscovery.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "unauthorized_devices")
public class UnauthorizedDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ip_address")
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
    
    @Column(name = "first_detected")
    private LocalDateTime firstDetected;
    
    @Column(name = "last_seen")
    private LocalDateTime lastSeen;
    
    @Column(name = "detection_count")
    private Integer detectionCount = 1;
    
    @Column(name = "is_investigated")
    private Boolean isInvestigated = false;
    
    @Column(name = "risk_level")
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel = RiskLevel.MEDIUM;
    
    @Column(name = "notes")
    private String notes;
    
    // Constructors
    public UnauthorizedDevice() {
        this.firstDetected = LocalDateTime.now();
        this.lastSeen = LocalDateTime.now();
        this.status = DeviceStatus.ONLINE;
    }
    
    public UnauthorizedDevice(String ipAddress, String macAddress) {
        this();
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    
    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }
    
    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }
    
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    
    public DeviceStatus getStatus() { return status; }
    public void setStatus(DeviceStatus status) { this.status = status; }
    
    public LocalDateTime getFirstDetected() { return firstDetected; }
    public void setFirstDetected(LocalDateTime firstDetected) { this.firstDetected = firstDetected; }
    
    public LocalDateTime getLastSeen() { return lastSeen; }
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }
    
    public Integer getDetectionCount() { return detectionCount; }
    public void setDetectionCount(Integer detectionCount) { this.detectionCount = detectionCount; }
    
    public Boolean getIsInvestigated() { return isInvestigated; }
    public void setIsInvestigated(Boolean isInvestigated) { this.isInvestigated = isInvestigated; }
    
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

// File: src/main/java/com/networkdiscovery/entity/DeviceStatus.java
package com.networkdiscovery.entity;

public enum DeviceStatus {
    ONLINE, OFFLINE, UNKNOWN
}

// File: src/main/java/com/networkdiscovery/entity/RiskLevel.java
package com.networkdiscovery.entity;

public enum RiskLevel {
    LOW, MEDIUM, HIGH, CRITICAL
}

// File: src/main/java/com/networkdiscovery/repository/AuthorizedDeviceRepository.java
package com.networkdiscovery.repository;

import com.networkdiscovery.entity.AuthorizedDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AuthorizedDeviceRepository extends JpaRepository<AuthorizedDevice, Long> {
    Optional<AuthorizedDevice> findByIpAddress(String ipAddress);
    Optional<AuthorizedDevice> findByMacAddress(String macAddress);
    List<AuthorizedDevice> findByIsActive(Boolean isActive);
    List<AuthorizedDevice> findByDeviceType(String deviceType);
    List<AuthorizedDevice> findByDepartment(String department);
    boolean existsByIpAddress(String ipAddress);
    boolean existsByMacAddress(String macAddress);
}

// File: src/main/java/com/networkdiscovery/repository/UnauthorizedDeviceRepository.java
package com.networkdiscovery.repository;

import com.networkdiscovery.entity.UnauthorizedDevice;
import com.networkdiscovery.entity.DeviceStatus;
import com.networkdiscovery.entity.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface UnauthorizedDeviceRepository extends JpaRepository<UnauthorizedDevice, Long> {
    Optional<UnauthorizedDevice> findByIpAddress(String ipAddress);
    Optional<UnauthorizedDevice> findByMacAddress(String macAddress);
    List<UnauthorizedDevice> findByStatus(DeviceStatus status);
    List<UnauthorizedDevice> findByRiskLevel(RiskLevel riskLevel);
    List<UnauthorizedDevice> findByIsInvestigated(Boolean isInvestigated);
    List<UnauthorizedDevice> findByLastSeenBefore(LocalDateTime dateTime);
    List<UnauthorizedDevice> findByFirstDetectedAfter(LocalDateTime dateTime);
}

// File: src/main/java/com/networkdiscovery/service/NetworkScannerService.java
package com.networkdiscovery.service;

import com.networkdiscovery.entity.*;
import com.networkdiscovery.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;

@Service
public class NetworkScannerService {
    
    private static final Logger log = LoggerFactory.getLogger(NetworkScannerService.class);
    
    @Autowired
    private AuthorizedDeviceRepository authorizedRepository;
    
    @Autowired
    private UnauthorizedDeviceRepository unauthorizedRepository;
    
    @Value("${network.subnet:192.168.1.0/24}")
    private String networkSubnet;
    
    public List<UnauthorizedDevice> scanForUnauthorizedDevices() {
        log.info("Starting unauthorized device scan for subnet: {}", networkSubnet);
        List<UnauthorizedDevice> newUnauthorizedDevices = new ArrayList<>();
        
        try {
            // Get ARP table entries
            List<ArpEntry> arpEntries = parseArpTable();
            
            // Perform ping sweep for active devices
            List<String> activeIps = performPingSweep();
            
            // Process each discovered device
            for (ArpEntry entry : arpEntries) {
                if (!isDeviceAuthorized(entry.getIpAddress(), entry.getMacAddress())) {
                    UnauthorizedDevice device = processUnauthorizedDevice(entry);
                    
                    // Update status based on ping results
                    if (activeIps.contains(entry.getIpAddress())) {
                        device.setStatus(DeviceStatus.ONLINE);
                        device.setLastSeen(LocalDateTime.now());
                    } else {
                        device.setStatus(DeviceStatus.OFFLINE);
                    }
                    
                    newUnauthorizedDevices.add(device);
                    log.warn("Unauthorized device detected: IP={}, MAC={}", 
                            entry.getIpAddress(), entry.getMacAddress());
                }
            }
            
            // Save unauthorized devices
            saveUnauthorizedDevices(newUnauthorizedDevices);
            
        } catch (Exception e) {
            log.error("Error during unauthorized device scan", e);
        }
        
        return newUnauthorizedDevices;
    }
    
    private boolean isDeviceAuthorized(String ipAddress, String macAddress) {
        return authorizedRepository.existsByIpAddress(ipAddress) || 
               authorizedRepository.existsByMacAddress(macAddress);
    }
    
    private UnauthorizedDevice processUnauthorizedDevice(ArpEntry entry) {
        Optional<UnauthorizedDevice> existing = unauthorizedRepository
                .findByMacAddress(entry.getMacAddress());
        
        UnauthorizedDevice device;
        if (existing.isPresent()) {
            device = existing.get();
            device.setLastSeen(LocalDateTime.now());
            device.setDetectionCount(device.getDetectionCount() + 1);
            
            // Update IP if changed
            if (!entry.getIpAddress().equals(device.getIpAddress())) {
                device.setIpAddress(entry.getIpAddress());
            }
        } else {
            device = new UnauthorizedDevice(entry.getIpAddress(), entry.getMacAddress());
            device.setVendor(lookupMacVendor(entry.getMacAddress()));
            device.setHostname(resolveHostname(entry.getIpAddress()));
            device.setDeviceType(determineDeviceType(device.getVendor(), device.getHostname()));
            device.setRiskLevel(assessRiskLevel(device));
        }
        
        return device;
    }
    
    private RiskLevel assessRiskLevel(UnauthorizedDevice device) {
        // Simple risk assessment logic
        String vendor = device.getVendor();
        String hostname = device.getHostname();
        
        if (vendor != null && (vendor.toLowerCase().contains("vm") || 
                              vendor.toLowerCase().contains("virtual"))) {
            return RiskLevel.HIGH; // Virtual machines could be suspicious
        }
        
        if (hostname != null && hostname.toLowerCase().contains("android")) {
            return RiskLevel.LOW; // Mobile devices are typically low risk
        }
        
        return RiskLevel.MEDIUM; // Default risk level
    }
    
    private String determineDeviceType(String vendor, String hostname) {
        if (vendor == null && hostname == null) return "Unknown";
        
        String combined = (vendor + " " + hostname).toLowerCase();
        
        if (combined.contains("apple") || combined.contains("iphone") || combined.contains("ipad")) {
            return "Apple Device";
        } else if (combined.contains("samsung") || combined.contains("android")) {
            return "Android Device";
        } else if (combined.contains("vm") || combined.contains("virtual")) {
            return "Virtual Machine";
        } else if (combined.contains("printer") || combined.contains("canon") || combined.contains("hp")) {
            return "Printer";
        } else if (combined.contains("router") || combined.contains("switch")) {
            return "Network Device";
        }
        
        return "Computer";
    }
    
    private List<ArpEntry> parseArpTable() throws IOException {
        List<ArpEntry> entries = new ArrayList<>();
        
        String os = System.getProperty("os.name").toLowerCase();
        String command = os.contains("win") ? "arp -a" : "arp -a";
        
        Process process = Runtime.getRuntime().exec(command);
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
        // Windows: "  192.168.1.100    aa-bb-cc-dd-ee-ff     dynamic"
        // Linux: "hostname (192.168.1.100) at aa:bb:cc:dd:ee:ff [ether] on eth0"
        
        Pattern windowsPattern = Pattern.compile("\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+([a-fA-F0-9-]{17})");
        Pattern linuxPattern = Pattern.compile("\\((\\d+\\.\\d+\\.\\d+\\.\\d+)\\)\\s+at\\s+([a-fA-F0-9:]{17})");
        
        Matcher windowsMatcher = windowsPattern.matcher(line);
        Matcher linuxMatcher = linuxPattern.matcher(line);
        
        if (windowsMatcher.find()) {
            String ip = windowsMatcher.group(1);
            String mac = windowsMatcher.group(2).replace("-", ":").toLowerCase();
            return new ArpEntry(ip, mac);
        } else if (linuxMatcher.find()) {
            String ip = linuxMatcher.group(1);
            String mac = linuxMatcher.group(2).toLowerCase();
            return new ArpEntry(ip, mac);
        }
        
        return null;
    }
    
    private List<String> performPingSweep() {
        List<String> activeIps = new ArrayList<>();
        String[] subnetParts = networkSubnet.split("/");
        String baseIp = subnetParts[0].substring(0, subnetParts[0].lastIndexOf('.'));
        
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
            return address.isReachable(1000);
        } catch (IOException e) {
            return false;
        }
    }
    
    private String lookupMacVendor(String macAddress) {
        String oui = macAddress.substring(0, 8).replace(":", "").toUpperCase();
        Map<String, String> vendors = Map.of(
            "00:50:56", "VMware",
            "08:00:27", "VirtualBox",
            "00:0C:29", "VMware",
            "00:15:5D", "Microsoft",
            "00:16:3E", "Xen",
            "52:54:00", "QEMU",
            "00:1B:21", "Intel",
            "00:23:24", "Apple"
        );
        return vendors.getOrDefault(oui, "Unknown");
    }
    
    private String resolveHostname(String ipAddress) {
        try {
            InetAddress addr = InetAddress.getByName(ipAddress);
            String hostname = addr.getHostName();
            return hostname.equals(ipAddress) ? null : hostname;
        } catch (Exception e) {
            return null;
        }
    }
    
    private void saveUnauthorizedDevices(List<UnauthorizedDevice> devices) {
        for (UnauthorizedDevice device : devices) {
            unauthorizedRepository.save(device);
        }
    }
}

// File: src/main/java/com/networkdiscovery/service/ArpEntry.java
package com.networkdiscovery.service;

public class ArpEntry {
    private String ipAddress;
    private String macAddress;
    
    public ArpEntry(String ipAddress, String macAddress) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
    }
    
    public String getIpAddress() { return ipAddress; }
    public String getMacAddress() { return macAddress; }
}

// File: src/main/java/com/networkdiscovery/controller/AuthorizedDeviceController.java
package com.networkdiscovery.controller;

import com.networkdiscovery.entity.AuthorizedDevice;
import com.networkdiscovery.repository.AuthorizedDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authorized-devices")
@CrossOrigin(origins = "*")
public class AuthorizedDeviceController {
    
    @Autowired
    private AuthorizedDeviceRepository repository;
    
    @GetMapping
    public ResponseEntity<List<AuthorizedDevice>> getAllAuthorizedDevices() {
        List<AuthorizedDevice> devices = repository.findByIsActive(true);
        return ResponseEntity.ok(devices);
    }
    
    @PostMapping
    public ResponseEntity<AuthorizedDevice> addAuthorizedDevice(@RequestBody AuthorizedDevice device) {
        device.setAddedBy("admin"); // In real app, get from security context
        AuthorizedDevice saved = repository.save(device);
        return ResponseEntity.ok(saved);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AuthorizedDevice> updateAuthorizedDevice(
            @PathVariable Long id, @RequestBody AuthorizedDevice device) {
        Optional<AuthorizedDevice> existing = repository.findById(id);
        if (existing.isPresent()) {
            device.setId(id);
            AuthorizedDevice updated = repository.save(device);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthorizedDevice(@PathVariable Long id) {
        Optional<AuthorizedDevice> device = repository.findById(id);
        if (device.isPresent()) {
            AuthorizedDevice dev = device.get();
            dev.setIsActive(false);
            repository.save(dev);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<AuthorizedDevice>> searchDevices(
            @RequestParam(required = false) String deviceType,
            @RequestParam(required = false) String department) {
        
        List<AuthorizedDevice> devices;
        if (deviceType != null) {
            devices = repository.findByDeviceType(deviceType);
        } else if (department != null) {
            devices = repository.findByDepartment(department);
        } else {
            devices = repository.findByIsActive(true);
        }
        
        return ResponseEntity.ok(devices);
    }
}

// File: src/main/java/com/networkdiscovery/controller/UnauthorizedDeviceController.java
package com.networkdiscovery.controller;

import com.networkdiscovery.entity.UnauthorizedDevice;
import com.networkdiscovery.entity.RiskLevel;
import com.networkdiscovery.repository.UnauthorizedDeviceRepository;
import com.networkdiscovery.service.NetworkScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/unauthorized-devices")
@CrossOrigin(origins = "*")
public class UnauthorizedDeviceController {
    
    @Autowired
    private UnauthorizedDeviceRepository repository;
    
    @Autowired
    private NetworkScannerService scannerService;
    
    @GetMapping
    public ResponseEntity<List<UnauthorizedDevice>> getAllUnauthorizedDevices() {
        List<UnauthorizedDevice> devices = repository.findAll();
        return ResponseEntity.ok(devices);
    }
    
    @GetMapping("/by-risk/{riskLevel}")
    public ResponseEntity<List<UnauthorizedDevice>> getDevicesByRisk(@PathVariable RiskLevel riskLevel) {
        List<UnauthorizedDevice> devices = repository.findByRiskLevel(riskLevel);
        return ResponseEntity.ok(devices);
    }
    
    @GetMapping("/uninvestigated")
    public ResponseEntity<List<UnauthorizedDevice>> getUninvestigatedDevices() {
        List<UnauthorizedDevice> devices = repository.findByIsInvestigated(false);
        return ResponseEntity.ok(devices);
    }
    
    @PostMapping("/scan")
    public ResponseEntity<List<UnauthorizedDevice>> performUnauthorizedScan() {
        List<UnauthorizedDevice> newDevices = scannerService.scanForUnauthorizedDevices();
        return ResponseEntity.ok(newDevices);
    }
    
    @PutMapping("/{id}/investigate")
    public ResponseEntity<UnauthorizedDevice> markAsInvestigated(@PathVariable Long id) {
        Optional<UnauthorizedDevice> device = repository.findById(id);
        if (device.isPresent()) {
            UnauthorizedDevice dev = device.get();
            dev.setIsInvestigated(true);
            repository.save(dev);
            return ResponseEntity.ok(dev);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/risk-level")
    public ResponseEntity<UnauthorizedDevice> updateRiskLevel(
            @PathVariable Long id, @RequestBody Map<String, String> request) {
        Optional<UnauthorizedDevice> device = repository.findById(id);
        if (device.isPresent()) {
            UnauthorizedDevice dev = device.get();
            dev.setRiskLevel(RiskLevel.valueOf(request.get("riskLevel")));
            repository.save(dev);
            return ResponseEntity.ok(dev);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/notes")
    public ResponseEntity<UnauthorizedDevice> updateNotes(
            @PathVariable Long id, @RequestBody Map<String, String> request) {
        Optional<UnauthorizedDevice> device = repository.findById(id);
        if (device.isPresent()) {
            UnauthorizedDevice dev = device.get();
            dev.setNotes(request.get("notes"));
            repository.save(dev);
            return ResponseEntity.ok(dev);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUnauthorizedStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUnauthorized", repository.count());
        stats.put("highRisk", repository.findByRiskLevel(RiskLevel.HIGH).size());
        stats.put("uninvestigated", repository.findByIsInvestigated(false).size());
        return ResponseEntity.ok(stats);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnauthorizedDevice(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

// File: src/main/java/com/networkdiscovery/controller/DashboardController.java
package com.networkdiscovery.controller;

import com.networkdiscovery.repository.AuthorizedDeviceRepository;
import com.networkdiscovery.repository.UnauthorizedDeviceRepository;
import com.networkdiscovery.entity.RiskLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    
    @Autowired
    private AuthorizedDeviceRepository authorizedRepository;
    
    @Autowired
    private UnauthorizedDeviceRepository unauthorizedRepository;
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Authorized device stats
        stats.put("totalAuthorized", authorizedRepository.findByIsActive(true).size());
        
        // Unauthorized device stats
        stats.put("totalUnauthorized", unauthorizedRepository.count());
        stats.put("highRiskDevices", unauthorizedRepository.findByRiskLevel(RiskLevel.HIGH).size());
        stats.put("criticalRiskDevices", unauthorizedRepository.findByRiskLevel(RiskLevel.CRITICAL).size());
        stats.put("uninvestigatedDevices", unauthorizedRepository.findByIsInvestigated(false).size());
        
        return ResponseEntity.ok(stats);
    }
}

// File: src/main/java/com/networkdiscovery/scheduler/NetworkScanScheduler.java
package com.networkdiscovery.scheduler;

import com.networkdiscovery.service.NetworkScannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NetworkScanScheduler {
    
    private static final Logger log = LoggerFactory.getLogger(NetworkScanScheduler.class);
    
    @Autowired
    private NetworkScannerService scannerService;
    
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void scheduledUnauthorizedDeviceScan() {
        log.info("Running scheduled unauthorized device scan");
        scannerService.scanForUnauthorizedDevices();
    }
}