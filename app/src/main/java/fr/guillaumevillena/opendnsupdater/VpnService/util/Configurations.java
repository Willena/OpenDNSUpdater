package fr.guillaumevillena.opendnsupdater.VpnService.util;

import java.util.ArrayList;

import fr.guillaumevillena.opendnsupdater.VpnService.util.server.CustomDNSServer;

public class Configurations {
    private static final int CUSTOM_ID_START = 32;

    private ArrayList<CustomDNSServer> customDNSServers;

    private int totalDnsId;

    public Configurations() {
        //TODO: Initial config. Eg. Build-in rules
    }

    public int getNextDnsId() {
        if (totalDnsId < CUSTOM_ID_START) {
            totalDnsId = CUSTOM_ID_START;
        }
        return totalDnsId++;
    }

    public ArrayList<CustomDNSServer> getCustomDNSServers() {
        if (customDNSServers == null) {
            customDNSServers = new ArrayList<>();
        }
        return customDNSServers;
    }

}
