Setting up a flow with the SensoreTag node

# Requirements on Raspberry Pi

The needed library can be installed by running:

* `sudo apt-get install libbluetooth-dev libudev-dev pi-bluetooth` (all bluez stack is needed `bluez bluez-libs bluez-libs-devel`)

# Requirements on Fedora

The corresponding libraries are:

* `libbluetooth-dev` on Debian -> `bluez-libs-devel` on Fedora (all bluez stack is needed `bluez bluez-libs bluez-libs-devel`)
* `libudev` on Debian -> `systemd-devel` on Fedora

# SensorTag Node-Red node

You could use this SensorTag Node-Red Node [https://www.npmjs.com/package/node-red-node-sensortag](https://www.npmjs.com/package/node-red-node-sensortag) but it doesn't work starting from NodeJS 10 anymore due to the following [issue](https://github.com/noble/node-bluetooth-hci-socket/issues/84).

Starting from the patched [node-bluetooth-hci-socket](https://www.npmjs.com/package/@abandonware/bluetooth-hci-socket) node module, I have forked all the modules in the pipeline for having the SensorTag Node-Red Node working again.
Following the pipeline:

* [node-red-node-sensortag](https://www.npmjs.com/package/node-red-node-sensortag)
* [node-sensortag](https://www.npmjs.com/package/sensortag)
* [noble-device](https://www.npmjs.com/package/noble-device)
* [noble](https://www.npmjs.com/package/noble)
* [node-bluetooth-hci-socket](https://www.npmjs.com/package/@abandonware/bluetooth-hci-socket)

I forked the related projects:

* [https://github.com/ppatierno/node-red-nodes](https://github.com/ppatierno/node-red-nodes)
* [https://github.com/ppatierno/node-sensortag](https://github.com/ppatierno/node-sensortag)
* [https://github.com/ppatierno/noble-device](https://github.com/ppatierno/noble-device)
* [https://github.com/ppatierno/noble](https://github.com/ppatierno/noble)

I republished the package in my own npm account:

[https://www.npmjs.com/~ppatierno](https://www.npmjs.com/~ppatierno)

Finally the SensorTag Node-Red node to use is:

`@ppatierno/node-red-node-sensortag`