OpenDNS updater
==============
OpenDNS Updater is an extremely easy to use android app to allow dynamic ip updates.
### Why this project ?
My personal use of this is to allow me to put a dns parental control by using openDNS definitions. As openDNS use the external ip address to apply the parental control,when connection are made outside your house, you need to update the ip address on openDNS servers. So this little app is made to update external ip address each time the android device is connected to a wifi network.

### Installation

#### Easiest way to install:

Go into the released category of the repo and download the latest released .apk
On your Android device you have to do some manipulation before installing the apk.
- Go into preferences
- Click on the security slide
- Allow unknown sources
- Open the apk (form the device)
- Install it
- Enjoy !!

#### Developer install

If you are a developer and want to use the app after a few changes on the code ( or not )
It is not currently the easiest way but it work.
- clone the repo
```sh
$ git clone [git-repo-url] openDNS Updater
```
- open the project in your android studio don't forget to download APIs at least for Android 4.4.4
- Enable and switch to developer mode on your device
- Build and Install via the Android Studio the app on your device

### Development

Want to contribute ? Great !
Git is made for ! Just fork, edit, and send a pull-request
