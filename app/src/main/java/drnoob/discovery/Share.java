package drnoob.discovery;

import android.net.nsd.NsdServiceInfo;

import java.util.LinkedList;

public class Share {
    private static Share onlyInstance = new Share();
    private static LinkedList<NsdServiceInfo> hosts;

    private Share() {
        hosts = new LinkedList<>();
    }

    public static Share getOnlyInstance() { return onlyInstance; }

    public static void addHost(NsdServiceInfo h) { hosts.add(h); }

    public static LinkedList<NsdServiceInfo> getHosts() { return  hosts; }
}
