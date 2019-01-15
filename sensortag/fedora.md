Some steps for interacting with SensorTag from Fedora

The `bluez` library is needed.

    sudo dnf install bluez bluez-libs

Checking for the Bluetooth device (dongle/onboard) on Fedora laptop

```
[ppatiern@new-host-3 ~]$ sudo hcitool dev
Devices:
	hci0	58:91:CF:C0:20:EB
```

Scan for Bluetooth devices

```
[ppatiern@new-host-3 ~]$ sudo hcitool lescan 
LE Scan ...
68:C9:0B:06:98:89 (unknown)
68:C9:0B:06:98:89 CC2650 SensorTag
47:AE:3A:E6:4E:85 (unknown)
47:AE:3A:E6:4E:85 (unknown)
E4:04:39:40:B7:19 TomTom G
E4:04:39:40:B7:19 TomTom GPS Watch
C0:E5:18:1B:CC:92 ID107Plus HR
C0:E5:18:1B:CC:92 (unknown)
FE:62:B8:EC:D3:49 Tile
FE:62:B8:EC:D3:49 (unknown)
```

Establish connection with the SensorTag

```
[ppatiern@new-host-3 ~]$ sudo hcitool lecc 68:C9:0B:06:98:89
Connection handle 3585
```

Disconnect from the SensorTag

```
[ppatiern@new-host-3 ~]$ sudo hcitool ledc 3585
```

Or we can use the `gatttool` for interacting with the SensorTag (`-b` address, `-I` in interactive mode)

```
[ppatiern@new-host-3 ~]$ sudo gatttool -b 68:C9:0B:06:98:89 -I
[68:C9:0B:06:98:89][LE]>
```

Connecting

```
[68:C9:0B:06:98:89][LE]> connect
Attempting to connect to 68:C9:0B:06:98:89
Connection successful
[68:C9:0B:06:98:89][LE]>
```

Asking for characteristics

```
[68:C9:0B:06:98:89][LE]> characteristics
handle: 0x0002, char properties: 0x02, char value handle: 0x0003, uuid: 00002a00-0000-1000-8000-00805f9b34fb
handle: 0x0004, char properties: 0x02, char value handle: 0x0005, uuid: 00002a01-0000-1000-8000-00805f9b34fb
handle: 0x0006, char properties: 0x02, char value handle: 0x0007, uuid: 00002a04-0000-1000-8000-00805f9b34fb
handle: 0x0009, char properties: 0x20, char value handle: 0x000a, uuid: 00002a05-0000-1000-8000-00805f9b34fb
handle: 0x000d, char properties: 0x02, char value handle: 0x000e, uuid: 00002a23-0000-1000-8000-00805f9b34fb
handle: 0x000f, char properties: 0x02, char value handle: 0x0010, uuid: 00002a24-0000-1000-8000-00805f9b34fb
handle: 0x0011, char properties: 0x02, char value handle: 0x0012, uuid: 00002a25-0000-1000-8000-00805f9b34fb
handle: 0x0013, char properties: 0x02, char value handle: 0x0014, uuid: 00002a26-0000-1000-8000-00805f9b34fb
handle: 0x0015, char properties: 0x02, char value handle: 0x0016, uuid: 00002a27-0000-1000-8000-00805f9b34fb
handle: 0x0017, char properties: 0x02, char value handle: 0x0018, uuid: 00002a28-0000-1000-8000-00805f9b34fb
handle: 0x0019, char properties: 0x02, char value handle: 0x001a, uuid: 00002a29-0000-1000-8000-00805f9b34fb
handle: 0x001b, char properties: 0x02, char value handle: 0x001c, uuid: 00002a2a-0000-1000-8000-00805f9b34fb
handle: 0x001d, char properties: 0x02, char value handle: 0x001e, uuid: 00002a50-0000-1000-8000-00805f9b34fb
handle: 0x0020, char properties: 0x12, char value handle: 0x0021, uuid: f000aa01-0451-4000-b000-000000000000
handle: 0x0023, char properties: 0x0a, char value handle: 0x0024, uuid: f000aa02-0451-4000-b000-000000000000
handle: 0x0025, char properties: 0x0a, char value handle: 0x0026, uuid: f000aa03-0451-4000-b000-000000000000
handle: 0x0028, char properties: 0x12, char value handle: 0x0029, uuid: f000aa21-0451-4000-b000-000000000000
handle: 0x002b, char properties: 0x0a, char value handle: 0x002c, uuid: f000aa22-0451-4000-b000-000000000000
handle: 0x002d, char properties: 0x0a, char value handle: 0x002e, uuid: f000aa23-0451-4000-b000-000000000000
handle: 0x0030, char properties: 0x12, char value handle: 0x0031, uuid: f000aa41-0451-4000-b000-000000000000
handle: 0x0033, char properties: 0x0a, char value handle: 0x0034, uuid: f000aa42-0451-4000-b000-000000000000
handle: 0x0035, char properties: 0x0a, char value handle: 0x0036, uuid: f000aa44-0451-4000-b000-000000000000
handle: 0x0038, char properties: 0x12, char value handle: 0x0039, uuid: f000aa81-0451-4000-b000-000000000000
handle: 0x003b, char properties: 0x0a, char value handle: 0x003c, uuid: f000aa82-0451-4000-b000-000000000000
handle: 0x003d, char properties: 0x0a, char value handle: 0x003e, uuid: f000aa83-0451-4000-b000-000000000000
handle: 0x0040, char properties: 0x12, char value handle: 0x0041, uuid: f000aa71-0451-4000-b000-000000000000
handle: 0x0043, char properties: 0x0a, char value handle: 0x0044, uuid: f000aa72-0451-4000-b000-000000000000
handle: 0x0045, char properties: 0x0a, char value handle: 0x0046, uuid: f000aa73-0451-4000-b000-000000000000
handle: 0x0048, char properties: 0x10, char value handle: 0x0049, uuid: 0000ffe1-0000-1000-8000-00805f9b34fb
handle: 0x004d, char properties: 0x0a, char value handle: 0x004e, uuid: f000aa65-0451-4000-b000-000000000000
handle: 0x004f, char properties: 0x0a, char value handle: 0x0050, uuid: f000aa66-0451-4000-b000-000000000000
handle: 0x0052, char properties: 0x1a, char value handle: 0x0053, uuid: f000ac01-0451-4000-b000-000000000000
handle: 0x0055, char properties: 0x0a, char value handle: 0x0056, uuid: f000ac02-0451-4000-b000-000000000000
handle: 0x0057, char properties: 0x0a, char value handle: 0x0058, uuid: f000ac03-0451-4000-b000-000000000000
handle: 0x005a, char properties: 0x12, char value handle: 0x005b, uuid: f000ccc1-0451-4000-b000-000000000000
handle: 0x005d, char properties: 0x08, char value handle: 0x005e, uuid: f000ccc2-0451-4000-b000-000000000000
handle: 0x005f, char properties: 0x08, char value handle: 0x0060, uuid: f000ccc3-0451-4000-b000-000000000000
handle: 0x0062, char properties: 0x1c, char value handle: 0x0063, uuid: f000ffc1-0451-4000-b000-000000000000
handle: 0x0066, char properties: 0x1c, char value handle: 0x0067, uuid: f000ffc2-0451-4000-b000-000000000000
[68:C9:0B:06:98:89][LE]> 
```

From the SensorTag guide [here](http://e2e.ti.com/cfs-file/__key/communityserver-discussions-components-files/538/attr_5F00_cc2650-sensortag.html) the handle 0x3 is the "Device Name"

```
[68:C9:0B:06:98:89][LE]> char-read-hnd 0x3
Characteristic value/descriptor: 53 65 6e 73 6f 72 54 61 67 20 32 2e 30 
```

Using Python decoder for getting the name

```
[ppatiern@new-host-3 ~]$ python
Python 2.7.15 (default, Oct 15 2018, 15:26:09) 
[GCC 8.2.1 20180801 (Red Hat 8.2.1-2)] on linux2
Type "help", "copyright", "credits" or "license" for more information.
>>> "53656e736f7254616720322e30".decode("hex")
'SensorTag 2.0'
>>> 
```