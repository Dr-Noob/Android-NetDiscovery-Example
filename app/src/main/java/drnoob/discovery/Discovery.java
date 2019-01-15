package drnoob.discovery;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import static android.net.nsd.NsdManager.FAILURE_ALREADY_ACTIVE;
import static android.net.nsd.NsdManager.FAILURE_INTERNAL_ERROR;
import static android.net.nsd.NsdManager.FAILURE_MAX_LIMIT;

public class Discovery {
    private static Discovery onlyInstance = new Discovery();
    private static Context ctx;
    private static NsdManager mNsdManager;
    private static boolean discoverEnabled;

    private static final String SERVICE_NAME = "NsdChat";
    private static final String SERVICE_TYPE = "_http._tcp.";
    private static final String DISCOVERY_TAG = "DISCOVERY";

    private Discovery() {
        discoverEnabled = false;
    }

    public static Discovery getOnlyInstance(Context newCtx) {
        if(ctx == null) {
            ctx = newCtx;
            mNsdManager = (NsdManager) ctx.getSystemService(Context.NSD_SERVICE);
        }
        return onlyInstance;
    }

    public boolean register() {
        registerService(2100);
        return true;
    }

    public boolean discover() {
        if(!discoverEnabled) {
            initializeDiscoveryListener();
            return true;
        }
        return false;
    }

    private void registerService(int port) {
        NsdManager.RegistrationListener mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name. Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                String mServiceName = NsdServiceInfo.getServiceName();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed! Put debugging code here to determine why.
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered. This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed. Put debugging code here to determine why.
            }
        };

        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(port);

        this.mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    public void initializeDiscoveryListener() {
        discoverEnabled = true;

        // Instantiate a new DiscoveryListener
        NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(DISCOVERY_TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found! Do something with it.
                Log.d(DISCOVERY_TAG, "Service discovery success" + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    // Service type is the string containing the protocol and
                    // transport layer for this service.
                    Log.d(DISCOVERY_TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(SERVICE_NAME)) {
                    // The name of the service tells the user what they'd be
                    // connecting to. It could be "Bob's Chat App".
                    Log.d(DISCOVERY_TAG, "Same machine: " + SERVICE_NAME);
                }
                initializeResolveListener(service);
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(DISCOVERY_TAG, "service lost: " + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(DISCOVERY_TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(DISCOVERY_TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(DISCOVERY_TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };

        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    public static synchronized void initializeResolveListener(NsdServiceInfo service) {

        NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                Log.e(DISCOVERY_TAG, "Resolve failed: ");
                switch (errorCode) {
                    case FAILURE_ALREADY_ACTIVE:
                        Log.e(DISCOVERY_TAG, "Operation is already active");
                        break;
                    case FAILURE_INTERNAL_ERROR:
                        Log.e(DISCOVERY_TAG, "NSD internal error");
                        break;
                    case FAILURE_MAX_LIMIT:
                        Log.e(DISCOVERY_TAG, "The maximum outstanding requests from the applications have reached");
                        break;
                    default:
                        Log.e(DISCOVERY_TAG, "Unknown error code");
                        break;
                }
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d(DISCOVERY_TAG, "Resolve Succeeded.");

                /*
                if (serviceInfo.getServiceName().equals(SERVICE_NAME)) {
                    Log.d(tag, "Same IP.");
                    return;
                }
                */

                /*
                int port = serviceInfo.getPort();
                InetAddress host = serviceInfo.getHost();

                //Log.d(DISCOVERY_TAG, host.getHostAddress() + ":" + port);
                Log.d(DISCOVERY_TAG, serviceInfo.toString());
                */
                Store.getOnlyInstance().addHost(serviceInfo);
            }
        };

        mNsdManager.resolveService(service, mResolveListener);
    }
}
