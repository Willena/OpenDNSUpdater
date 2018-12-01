package fr.guillaumevillena.opendnsupdater.VpnService.util.server;

import java.net.InetAddress;
import java.util.HashMap;

import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;


public class DNSServerHelper {
    private static HashMap<String, Integer> portCache = null;

    public static void clearPortCache() {
        portCache = null;
    }

    public static void buildPortCache() {
        portCache = new HashMap<>();
        for (DNSServer server : OpenDnsUpdater.DNS_SERVERS) {
            portCache.put(server.getAddress(), server.getPort());
        }
    }

    public static int getPortOrDefault(InetAddress address, int defaultPort) {
        String hostAddress = address.getHostAddress();

        if (portCache.containsKey(hostAddress)) {
            return portCache.get(hostAddress);
        }

        return defaultPort;
    }

    public static String getPrimary() {
        return String.valueOf(DNSServerHelper.checkServerId(0));
    }

    public static String getSecondary() {
        return String.valueOf(DNSServerHelper.checkServerId(1));
    }

    private static int checkServerId(int id) {
        if (id < OpenDnsUpdater.DNS_SERVERS.size() && id >= 0) {
            return id;
        }
        return 0;
    }

    public static String getAddressById(String id) {
        for (DNSServer server : OpenDnsUpdater.DNS_SERVERS) {
            if (server.getId().equals(id)) {
                return server.getAddress();
            }
        }
        return OpenDnsUpdater.DNS_SERVERS.get(0).getAddress();
    }
}
