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

    private static String SERVICE_NAME        = "NsdChat";
    private static final String SERVICE_TYPE  = "_http._tcp.";
    private static final String DISCOVERY_TAG = "DISCOVERY";
    private static final int SERVICE_PORT     = 2100;

    private static boolean discoverServiceUp;
    private static boolean registerServiceUp;

    private static NsdManager mNsdManager;
    private static NsdManager.RegistrationListener mRegistrationListener;
    private static NsdManager.DiscoveryListener mDiscoveryListener;

    private Discovery() {
        discoverServiceUp = false;
        registerServiceUp = false;
    }

    /* PUBLIC METHODS */

    public static Discovery getOnlyInstance(Context newCtx) {
        if(ctx == null) {
            ctx = newCtx;
            mNsdManager = (NsdManager) ctx.getSystemService(Context.NSD_SERVICE);
        }
        return onlyInstance;
    }

    public boolean register() {
        registerService(SERVICE_PORT);
        return true;
    }

    public boolean unregister() {
        unregisterService();
        return true;
    }

    public boolean startDiscover() {
        startDiscovery();
        return true;
    }

    public boolean stopDiscover() {
        stopDiscovery();
        return true;
    }

    public boolean discoverServiceUp() { return discoverServiceUp; }
    public boolean registerServiceUp() { return registerServiceUp; }

    /* PRIVATE METHODS */

    /* REGISTER METHODS */

    private void unregisterService() {
        registerServiceUp = false;
        mNsdManager.unregisterService(mRegistrationListener);
    }

    private void registerService(int port) {
        registerServiceUp = true;

        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                //Android may have changed it!
                SERVICE_NAME = NsdServiceInfo.getServiceName();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(DISCOVERY_TAG, "Registration failed: ");
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
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered. This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(DISCOVERY_TAG, "Unregistration failed: ");
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
        };

        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(port);

        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    /* DISCOVERY METHODS */

    private void stopDiscovery() {
        discoverServiceUp = false;
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    private void startDiscovery() {
        discoverServiceUp = true;

        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(DISCOVERY_TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(DISCOVERY_TAG, "Service discovery success: " + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    Log.d(DISCOVERY_TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(SERVICE_NAME)) {
                    Log.d(DISCOVERY_TAG, "Same machine: " + SERVICE_NAME);
                }
                resolve(service);
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
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

        // This call launches the previously defined (mDiscoveryListener) listener
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    public static void resolve(NsdServiceInfo service) {

        NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
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
                Share.getOnlyInstance().addHost(serviceInfo);
            }
        };

        // This call launches the previously defined (mResolveListener) listener
        mNsdManager.resolveService(service, mResolveListener);
    }
}
