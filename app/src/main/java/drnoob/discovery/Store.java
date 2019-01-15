package drnoob.discovery;

import android.net.nsd.NsdServiceInfo;

import java.util.LinkedList;

public class Store {
    private static Store onlyInstance = new Store();
    private static LinkedList<NsdServiceInfo> hosts;

    private Store() {
        hosts = new LinkedList<>();
    }

    public static Store getOnlyInstance() { return onlyInstance; }

    public static void addHost(NsdServiceInfo h) { hosts.add(h); }

    public static LinkedList<NsdServiceInfo> getHosts() { return  hosts; }
}
