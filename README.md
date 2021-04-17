[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java11](https://img.shields.io/badge/java-11-blue)](https://img.shields.io/badge/java-11-blue)
[![Gradle](https://img.shields.io/badge/gradle-v6.5-blue)](https://img.shields.io/badge/gradle-v6.5-blue)
![Build and Test](https://github.com/jveverka/java-11-examples/workflows/Build%20and%20Test/badge.svg)

# Java 11 examples
This repository contains various simple java 11 examples.
Examples are demonstrating not only new java 11 language features, 
but also new JDK 11 possibilities. 

### Environment setup
Make sure following software is installed on your PC.
* [OpenJDK 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
* [Gradle 6.8](https://gradle.org/install/) or later
* [docker.io 19.x](https://www.docker.com/) or later 

Please check [system requirements](docs/system-requirements.md) before. 

### Compile & Test
Most examples are build by top-level gradle project.
```
gradle clean build test
```

### Examples
* [artefact publishing demo](artefact-publishing-demo) - publish artefact to ORSSH.
* [akka clustering demo](akka-cluster-sshsessions)
* [futures demo](futures-demo)
* [image processing demo](imageprocessing-demo)
* [jetty server demo](jetty-servlet4-http2)
* [kafka example](kafka-example)
* [RxJava demo](rxjava-demo)
* [simple JNI demo](simple-jni-demo)
* [JPMS demo](simple-module-example)
* [ssh server demo](ssh-server-demo)
* [com.fasterxml.jackson](jackson-fasterxml-demo)
* [weird java stuff](java-is-weird)
* __Security and Crypto__
  * [Diffie-Hellman demo](diffie-hellman-demo) 
  * [JCE demo](jce-demo)
  * [Blockchain demo](block-chain)
  * [JWT demo](jwt-demo)
  * [Entropy calculation demo](entropy-demo)
  * [Enigma demo](enigma-demo)
* __Databases__
  * [mongodb demo](mongodb-demo)
  * [hibernate demo](hibernate-demo)
  * [JDBC demo](jdbc-demo)
  * [R2DBC demo](r2dbc-demo)
  * [Elastic Search demo](elastic-demo)

### Other Java Examples
* [spring demos](https://github.com/jveverka/spring-examples) - java11 & docker & gradle examples
* [java-boot-camp](https://github.com/jveverka/java-boot-camp) - java11 tutorials & maven examples

### JDK9 - JDK11 New Features 
* JDK9  [Feature list](https://openjdk.java.net/projects/jdk9/)
* JDK10 [Feature list](https://openjdk.java.net/projects/jdk/10/)
* JDK11 [Feature list](https://openjdk.java.net/projects/jdk/11/) 

### References
* [JDK12 - JDK17 Features](https://github.com/jveverka/java-17-examples)

_Enjoy !_
