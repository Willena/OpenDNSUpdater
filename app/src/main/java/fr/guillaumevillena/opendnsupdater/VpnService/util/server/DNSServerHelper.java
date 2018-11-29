package fr.guillaumevillena.opendnsupdater.VpnService.util.server;

import android.content.Context;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;
import fr.guillaumevillena.opendnsupdater.VpnService.service.OpenDnsVpnService;


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

    public static int getPosition(String id) {
        int intId = Integer.parseInt(id);
        if (intId < OpenDnsUpdater.DNS_SERVERS.size()) {
            return intId;
        }

        for (int i = 0; i < OpenDnsUpdater.configurations.getCustomDNSServers().size(); i++) {
            if (OpenDnsUpdater.configurations.getCustomDNSServers().get(i).getId().equals(id)) {
                return i + OpenDnsUpdater.DNS_SERVERS.size();
            }
        }
        return 0;
    }

    public static String getPrimary() {
        return String.valueOf(DNSServerHelper.checkServerId(0));
    }

    public static String getSecondary() {
        return String.valueOf(DNSServerHelper.checkServerId(0));
    }

    private static int checkServerId(int id) {
        if (id < OpenDnsUpdater.DNS_SERVERS.size()) {
            return id;
        }
        for (CustomDNSServer server : OpenDnsUpdater.configurations.getCustomDNSServers()) {
            if (server.getId().equals(String.valueOf(id))) {
                return id;
            }
        }
        return 0;
    }

    public static String getAddressById(String id) {
        for (DNSServer server : OpenDnsUpdater.DNS_SERVERS) {
            if (server.getId().equals(id)) {
                return server.getAddress();
            }
        }
        for (CustomDNSServer customDNSServer : OpenDnsUpdater.configurations.getCustomDNSServers()) {
            if (customDNSServer.getId().equals(id)) {
                return customDNSServer.getAddress();
            }
        }
        return OpenDnsUpdater.DNS_SERVERS.get(0).getAddress();
    }

    public static String[] getIds() {
        ArrayList<String> servers = new ArrayList<>(OpenDnsUpdater.DNS_SERVERS.size());
        for (DNSServer server : OpenDnsUpdater.DNS_SERVERS) {
            servers.add(server.getId());
        }
        for (CustomDNSServer customDNSServer : OpenDnsUpdater.configurations.getCustomDNSServers()) {
            servers.add(customDNSServer.getId());
        }
        String[] stringServers = new String[OpenDnsUpdater.DNS_SERVERS.size()];
        return servers.toArray(stringServers);
    }

    public static String[] getNames(Context context) {
        ArrayList<String> servers = new ArrayList<>(OpenDnsUpdater.DNS_SERVERS.size());
        for (DNSServer server : OpenDnsUpdater.DNS_SERVERS) {
            servers.add(server.getStringDescription(context));
        }
        for (CustomDNSServer customDNSServer : OpenDnsUpdater.configurations.getCustomDNSServers()) {
            servers.add(customDNSServer.getName());
        }
        String[] stringServers = new String[OpenDnsUpdater.DNS_SERVERS.size()];
        return servers.toArray(stringServers);
    }

    public static ArrayList<AbstractDNSServer> getAllServers() {
        ArrayList<AbstractDNSServer> servers = new ArrayList<>(OpenDnsUpdater.DNS_SERVERS.size());
        servers.addAll(OpenDnsUpdater.DNS_SERVERS);
        servers.addAll(OpenDnsUpdater.configurations.getCustomDNSServers());
        return servers;
    }

    public static String getDescription(String id, Context context) {
        for (DNSServer server : OpenDnsUpdater.DNS_SERVERS) {
            if (server.getId().equals(id)) {
                return server.getStringDescription(context);
            }
        }
        for (CustomDNSServer customDNSServer : OpenDnsUpdater.configurations.getCustomDNSServers()) {
            if (customDNSServer.getId().equals(id)) {
                return customDNSServer.getName();
            }
        }
        return OpenDnsUpdater.DNS_SERVERS.get(0).getStringDescription(context);
    }

    public static boolean isInUsing(CustomDNSServer server) {
        return OpenDnsVpnService.isActivated() && (server.getId().equals(getPrimary()) || server.getId().equals(getSecondary()));
    }
}
