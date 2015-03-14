mon4j
=====

Alert Monitoring for Java

*Combining Sigar, Netty, and Quartz*

To build:

The sigar-native-dependencies jar is in the lib/ directory. Ultimately you'd like to have this jar in your local maven repo for building and packaging.

***Currently, you'll need java 7 installed to compile / run.***

    mvn package

To run:

After maven package, you will have a mon4j jar, a lib folder, and a mon4j.properties file.

In the mon4j.properties file, you'll see properties like this:

* cpu.on=false
* cpu.armValue=9.0
* cpu.armDelay=60
* cpu.operator=percent
* cpu.reArmValue=5.0
* cpu.intervalInSeconds=5

The above block represents an ArmJob. Arm because they require a threshold value (based on the operator) to be crossed for a certain amount of time. The amount of time before triggering an alert is the armDelay. If the job falls below the reArmValue, then the job status resets to normal. 

The above example is more specifically, a CPU Job where cpu.on=true will turn this job on. If the CPU usage is above 9.0% for 60 seconds, then an alert will be triggered. The job runs every 5 seconds as specified by the intervalInSeconds property. If the CPU is above 9.0% for say, 30 seconds, and then dips below the reArmValue of 5.0% then everything gets reset. The DISK, CPU, and MEMORY jobs will follow the above pattern.

Please edit the mon4j.properties file, or at least take a look to see what's adjustable. Running the app is the standard:

    java -jar mon4j-0.1.0.jar

To do:

* ServiceJob and EventLogJob need lots of work
* PingJob needs to have alerting built
