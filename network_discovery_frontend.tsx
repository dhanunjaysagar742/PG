import React, { useState, useEffect } from 'react';
import { Search, Wifi, WifiOff, Shield, ShieldAlert, RefreshCw, Eye } from 'lucide-react';

const NetworkDeviceDashboard = () => {
  const [devices, setDevices] = useState([]);
  const [filteredDevices, setFilteredDevices] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [filter, setFilter] = useState('all');
  const [stats, setStats] = useState({});
  const [selectedDevice, setSelectedDevice] = useState(null);

  // API base URL - adjust for your backend
  const API_BASE = 'http://localhost:8080/api/devices';

  useEffect(() => {
    fetchDevices();
    fetchStats();
  }, []);

  useEffect(() => {
    filterDevices();
  }, [devices, searchTerm, filter]);

  const fetchDevices = async () => {
    try {
      const response = await fetch(API_BASE);
      const data = await response.json();
      setDevices(data);
    } catch (error) {
      console.error('Error fetching devices:', error);
    }
  };

  const fetchStats = async () => {
    try {
      const response = await fetch(`${API_BASE}/stats`);
      const data = await response.json();
      setStats(data);
    } catch (error) {
      console.error('Error fetching stats:', error);
    }
  };

  const performScan = async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE}/scan`, { method: 'POST' });
      const data = await response.json();
      setDevices(data);
      fetchStats();
    } catch (error) {
      console.error('Error performing scan:', error);
    } finally {
      setLoading(false);
    }
  };

  const authorizeDevice = async (deviceId) => {
    try {
      const response = await fetch(`${API_BASE}/${deviceId}/authorize`, { 
        method: 'PUT' 
      });
      if (response.ok) {
        fetchDevices();
        fetchStats();
      }
    } catch (error) {
      console.error('Error authorizing device:', error);
    }
  };

  const filterDevices = () => {
    let filtered = devices;

    // Apply status filter
    if (filter === 'online') {
      filtered = filtered.filter(device => device.status === 'ONLINE');
    } else if (filter === 'unauthorized') {
      filtered = filtered.filter(device => !device.isAuthorized);
    } else if (filter === 'offline') {
      filtered = filtered.filter(device => device.status === 'OFFLINE');
    }

    // Apply search filter
    if (searchTerm) {
      filtered = filtered.filter(device =>
        device.ipAddress?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        device.macAddress?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        device.hostname?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        device.vendor?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    setFilteredDevices(filtered);
  };

  const getStatusIcon = (status) => {
    return status === 'ONLINE' ? 
      <Wifi className="w-4 h-4 text-green-500" /> : 
      <WifiOff className="w-4 h-4 text-gray-400" />;
  };

  const getAuthIcon = (isAuthorized) => {
    return isAuthorized ? 
      <Shield className="w-4 h-4 text-blue-500" /> : 
      <ShieldAlert className="w-4 h-4 text-red-500" />;
  };

  const formatTimestamp = (timestamp) => {
    return new Date(timestamp).toLocaleString();
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Network Device Discovery</h1>
          <p className="text-gray-600 mt-2">Monitor and manage devices on your intranet</p>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center">
              <div className="p-2 bg-blue-100 rounded-lg">
                <Wifi className="w-6 h-6 text-blue-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Total Devices</p>
                <p className="text-2xl font-bold text-gray-900">{stats.totalDevices || 0}</p>
              </div>
            </div>
          </div>
          
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center">
              <div className="p-2 bg-green-100 rounded-lg">
                <Wifi className="w-6 h-6 text-green-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Online</p>
                <p className="text-2xl font-bold text-gray-900">{stats.onlineDevices || 0}</p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center">
              <div className="p-2 bg-red-100 rounded-lg">
                <ShieldAlert className="w-6 h-6 text-red-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Unauthorized</p>
                <p className="text-2xl font-bold text-gray-900">{stats.unauthorizedDevices || 0}</p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <button
              onClick={performScan}
              disabled={loading}
              className="w-full flex items-center justify-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50"
            >
              <RefreshCw className={`w-4 h-4 mr-2 ${loading ? 'animate-spin' : ''}`} />
              {loading ? 'Scanning...' : 'Scan Network'}
            </button>
          </div>
        </div>

        {/* Controls */}
        <div className="bg-white rounded-lg shadow mb-6 p-4">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1">
              <div className="relative">
                <Search className="w-5 h-5 absolute left-3 top-3 text-gray-400" />
                <input
                  type="text"
                  placeholder="Search by IP, MAC, hostname, or vendor..."
                  className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
            </div>
            
            <select
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
            >
              <option value="all">All Devices</option>
              <option value="online">Online Only</option>
              <option value="offline">Offline Only</option>
              <option value="unauthorized">Unauthorized Only</option>
            </select>
          </div>
        </div>

        {/* Device Table */}
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    IP Address
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    MAC Address
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Hostname
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Vendor
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Auth Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Last Seen
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredDevices.map((device) => (
                  <tr key={device.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        {getStatusIcon(device.status)}
                        <span className={`ml-2 text-sm ${device.status === 'ONLINE' ? 'text-green-600' : 'text-gray-400'}`}>
                          {device.status}
                        </span>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {device.ipAddress}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 font-mono">
                      {device.macAddress}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {device.hostname || 'Unknown'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {device.vendor || 'Unknown'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        {getAuthIcon(device.isAuthorized)}
                        <span className={`ml-2 text-sm ${device.isAuthorized ? 'text-blue-600' : 'text-red-600'}`}>
                          {device.isAuthorized ? 'Authorized' : 'Unauthorized'}
                        </span>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {device.lastSeen ? formatTimestamp(device.lastSeen) : 'Never'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <div className="flex space-x-2">
                        <button
                          onClick={() => setSelectedDevice(device)}
                          className="text-blue-600 hover:text-blue-900"
                        >
                          <Eye className="w-4 h-4" />
                        </button>
                        {!device.isAuthorized && (
                          <button
                            onClick={() => authorizeDevice(device.id)}
                            className="px-3 py-1 bg-green-100 text-green-800 rounded-full text-xs hover:bg-green-200"
                          >
                            Authorize
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {filteredDevices.length === 0 && (
          <div className="text-center py-8 text-gray-500">
            No devices found matching your criteria.
          </div>
        )}

        {/* Device Detail Modal */}
        {selectedDevice && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg max-w-md w-full p-6">
              <h3 className="text-lg font-semibold mb-4">Device Details</h3>
              <div className="space-y-3">
                <div>
                  <label className="text-sm font-medium text-gray-600">IP Address</label>
                  <p className="text-sm text-gray-900">{selectedDevice.ipAddress}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-gray-600">MAC Address</label>
                  <p className="text-sm text-gray-900 font-mono">{selectedDevice.macAddress}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-gray-600">Hostname</label>
                  <p className="text-sm text-gray-900">{selectedDevice.hostname || 'Unknown'}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-gray-600">Vendor</label>
                  <p className="text-sm text-gray-900">{selectedDevice.vendor || 'Unknown'}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-gray-600">First Seen</label>
                  <p className="text-sm text-gray-900">{formatTimestamp(selectedDevice.firstSeen)}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-gray-600">Last Seen</label>
                  <p className="text-sm text-gray-900">{formatTimestamp(selectedDevice.lastSeen)}</p>
                </div>
              </div>
              <div className="mt-6 flex justify-end">
                <button
                  onClick={() => setSelectedDevice(null)}
                  className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300"
                >
                  Close
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default NetworkDeviceDashboard;