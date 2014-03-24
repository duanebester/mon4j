mon4j
=====

Monitoring for Java

To build:

You will want to add the sigar-native-dependencies jar into your class path. The easiest way to do this is to install the jar to your local maven repo with mvn:install. But this isn't enough for Netbeans. So, in Netbeans, right click on the sigar-native-deps jar and say install locally, then browse to where you downloaded the jar, and choose it. This adds the native-deps jar to the local netbeans maven repo.

    mvn package

To Run:

After maven package, you will have a mon4j jar, a lib folder, and a mon4j.properties file.

Please edit the mon4j.properties file, or at least take a look to see what's adjustable.

    java -jar mon4j-0.1.0.jar
