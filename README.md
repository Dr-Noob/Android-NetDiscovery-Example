# Android-NetDiscovery-Example
This is just a very simple app (which can be used as example) using native Android network service discovery ([link](https://developer.android.com/training/connect-devices-wirelessly/nsd)) using __MDNS__. This means that two android devices running this app can be found each other, so they can establish a connection to acomplish any objective.

## Install
Just download the repo, open it with Android Studio and compile the app.

## Usage
To discover other devices in your network, you need to "scan" the network. This is done by using the "Start Discover" button (you can stop this service any time with "Stop Discover"). In the same way, if you would like to make a device visible to other devices, you need to
register it in the network with "Start register". Finally, "List" button
lets you see which devices have you disovered in the network, showing
its IP adress and port (which is defined at the source code)

<img src="image.jpg" alt="App example" width="200" height="400" />
