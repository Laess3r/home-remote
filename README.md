#home-remote

control your home over the web. all from your android device.

![alt text](https://raw.githubusercontent.com/DaHu4wA/home-remote/master/Server/resource/titleimage.png "Logo")

##Description

When it comes to simple software to control your home from an android device, a lot of projects exist. I wanted to do it my own way and make use of my [Raspberry Pi](http://www.amazon.de/gp/product/B008PT4GGC/ref=as_li_ss_tl?ie=UTF8&camp=1638&creative=19454&creativeASIN=B008PT4GGC&linkCode=as2&tag=da0d-21 Raspberry Pi) and my [Arduino](http://www.amazon.de/gp/product/B008GRTSV6/ref=as_li_ss_tl?ie=UTF8&camp=1638&creative=19454&creativeASIN=B008GRTSV6&linkCode=as2&tag=da0d-21). At the time when I started this project, the raspberry was not even available, so no frameworks etc. for using the I/Os of the raspi existed, also no display.

##Current Features
  * Control the plugs via Android, so you can turn on/off every device plugged in
  * See temperatures / humidity on the app
  * See a Log inside the android app
  * Do a Timing, so your lights will go out at specified time

![alt text](https://raw.githubusercontent.com/DaHu4wA/home-remote/master/Server/resource/images/allimages-small.jpg "Logo2")

![alt text](https://raw.githubusercontent.com/DaHu4wA/home-remote/master/Server/resource/images/displaysmall.jpg "Logo2")

##The whole setup consists of
  * Android application
  * Rasperry Pi server application
  * Small loop for Arduino with correct wiring
  * Remote plugs (like the ones from [Breenstuhl on Amazon](http://www.amazon.de/gp/product/B001AX8QUM/ref=as_li_ss_tl?ie=UTF8&camp=1638&creative=19454&creativeASIN=B001AX8QUM&linkCode=as2&tag=da0d-21), or Pollin)

##Current Implementation
  * The Android communicates via JSON to the Raspberry Server
  * The Server part is written in Java, using a Tomcat server
  * The Arduino is connected to the Raspi via USB
  * Connection between Raspi and Arduino is done via [http://rxtx.qbang.org/ RXTX library]
  * Temperature, timers etc are displayed on the display of the Arduno Display Shield (2 lines)

##Future Plans
  * Rewritten Server application (current in very early state) using a [h2-DB]([http://www.h2database.com/html/main.html )
  * Web interface using a state-of-the-art web technology
  * Get rid of the Arduino by accessing the I/O's of the Raspberry
  * A experimental JavaFx client can be found in the trunk

----

#Installation
The installation is split into different parts, not every one is quite simple.
I split the manual in different parts, so go through every part and follow the pages.

 _*Newer application files will be uploaded soon!*_

  # Setup the hardware with correct wirings (ArduinoSetUp)
  # Setup Rasperry Pi incl. tomcat (RaspberrySetUp)
  # Install the Android application (AndroidSetUp)
  # Access the Server from the www (Forward the port 3652 to your Pi)

