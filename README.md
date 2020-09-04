[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java11](https://img.shields.io/badge/java-11-blue)](https://img.shields.io/badge/java-11-blue)
[![Gradle](https://img.shields.io/badge/gradle-v6.5-blue)](https://img.shields.io/badge/gradle-v6.5-blue)
[![Build Status](https://travis-ci.com/jveverka/java-11-examples.svg?branch=master)](https://travis-ci.com/jveverka/java-11-examples)

# Java 11 examples
This repository contains various simple java 11 examples.
Examples are demonstrating not only new java 11 language features, 
but also new JDK 11 possibilities. 

### Environment setup
Make sure following software is installed on your PC.
* [OpenJDK 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
* [Gradle 6.5](https://gradle.org/install/) or later
* [protoc 3.8](https://github.com/protocolbuffers/protobuf/releases/tag/v3.8.0) google protocol buffers
* [docker.io 19.x](https://www.docker.com/) or later 

Please check [system requirements](docs/system-requirements.md) before. 

### Compile & Test
Most examples are build by top-level gradle project.
```
gradle clean build test
gradle --build-file di-examples/build.gradle clean test
```

### Examples
* [akka clustering demo](akka-cluster-sshsessions)
* [apache avro demo](avro-demo)
* [blockchain demo](block-chain)
* [dependency injection examples](di-examples)
* [futures demo](futures-demo)
* [grpc demo](grpc-demo)
* [hazelcast cluster demo](hazelcast-cluster)
* [image processing demo](imageprocessing-demo)
* [jetty server demo](jetty-servlet4-http2)
* [kafka example](kafka-example)
* [RxJava demo](rxjava-demo)
* [simple JNI demo](simple-jni-demo)
* [JPMS demo](simple-module-example)
* [ssh server demo](ssh-server-demo)
* [JCE demo](jce-demo)
* [com.fasterxml.jackson](jackson-fasterxml-demo)
* __Databases__
  * [mongodb demo](mongodb-demo)
  * [hibernate demo](hibernate-demo)
  * [JDBC demo](jdbc-demo)
  * [R2DBC demo](r2dbc-demo)

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
