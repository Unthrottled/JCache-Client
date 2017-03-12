# JCache-Client
A Quick example of showing how to us the JCache-Client backed by Hazelcast 3.4.2. 
Using an embedded Hazelcast server, and a Java hazelcast client. Then configuring the JCache api to supply caches with 10 second entry expiry backed by the hazelcast client. :)
### Perquisites
* Java 1.8 Installed
* Gradle 3.4.1 Installed

### Steps
1. Open a command line interface with a current working directory of this repository. ie (/path/to/JCache-Client).
2. Run the gradle task fatJar ie (gradle fatJar)
3. Run the jar int the build directory. ie (java -jar build/libs/JCache-Client-1.0.jar)
