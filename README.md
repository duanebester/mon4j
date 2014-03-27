mon4j
=====

Alert Monitoring for Java

*Combining Sigar, Netty, and Quartz*

[monty.io](https://monty.io)

To build:

The sigar-native-dependencies jar is in the lib/ directory. Ultimately you'd like to have this jar in your local maven repo for building and packaging.

***Currently, you'll need java 7 installed to compile / run.***

    mvn package

To run:

After maven package, you will have a mon4j jar, a lib folder, and a mon4j.properties file.

Please edit the mon4j.properties file, or at least take a look to see what's adjustable.

    java -jar mon4j-0.1.0.jar

To do:

* I'm at a loss with Ping, but will keep trying
* Need to implement EventLog job
