package fr.guillaumevillena.opendnsupdater.event;

public class InterfaceUpdatedEvent {
    private final String iface;

    public InterfaceUpdatedEvent(String iface) {

        this.iface = iface;
    }

    public String getIface() {
        return iface;
    }
}
