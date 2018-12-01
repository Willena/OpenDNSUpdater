package fr.guillaumevillena.opendnsupdater.vpnService.util.server;

import android.content.Context;

import fr.guillaumevillena.opendnsupdater.OpenDnsUpdater;

public class DNSServer extends AbstractDNSServer {

    private static int totalId = 0;

    private String id;
    private int description = 0;

    public DNSServer(String address, int description, int port) {
        super(address, port);
        this.id = String.valueOf(totalId++);
        this.description = description;
    }

    public DNSServer(String address, int description) {
        this(address, description, DNS_SERVER_DEFAULT_PORT);
    }

    public String getId() {
        return id;
    }

    public String getStringDescription(Context context) {
        return context.getResources().getString(description);
    }

    @Override
    public String getName() {
        return getStringDescription(OpenDnsUpdater.getInstance());
    }
}
