The following are the steps for setting up a Raspberry Pi 2.

# Download Raspbian image

* Download the "Raspbian Stretch with desktop" image at following [link](https://www.raspberrypi.org/downloads/raspbian/)
* Unzip the Raspian img file

# Flash Raspbian image on the microSD with Etcher

* Download the Etcher software for flashing the Raspbian image on a microSD card from [here](https://www.balena.io/etcher/)
* Insert the microSD
* Run Etcher (it recognizes the available microSD)
* Select the Raspian img file
* Start flashing!

# First Raspberry Pi 2 boot

* Configure the operating system on first boot (locale, keyboard, ...)
* Configure the WiFi network (the TP-LINK TL WN725N dongle works fine) or connect via Ethernet cable
* Connect a Bluetooth dongle (a compatible CSR 4.0 is fine)
* Enable SSH from the "Raspberry Pi Configuration" menu then "Interfaces" tab

# SSH access

* If you want to know the Raspberry Pi IP address from your laptop you can use `nmap` (i.e. `nmap -sn 192.168.1.0/24` if your laptop has an address in the `192.168.1.XXX` local network)
* Just run `ssh pi@<raspberry_pi_address>` and then enter the password (default is `raspberry`)

# Installing Node-Red

* SSH to the Raspberry Pi
* Follow instruction from the official Node-Red website [here](https://nodered.org/docs/hardware/raspberrypi) running the script