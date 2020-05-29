[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/jveverka/java-11-examples.svg?branch=master)](https://travis-ci.org/jveverka/java-11-examples)

# Java 11 examples
This repository contains various simple java 11 examples.
Examples are demonstrating not only new java 11 language features, 
but also new JDK 11 possibilities. 

### Environment setup
Make sure following software is installed on your PC.
* [OpenJDK 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
* [Gradle 6.3](https://gradle.org/install/) or later
* [protoc 3.8](https://github.com/protocolbuffers/protobuf/releases/tag/v3.8.0) - google protocol buffers

### Compile examples
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
* [hibernate demo](hibernate-demo)
* [image processing demo](imageprocessing-demo)
* [jetty server demo](jetty-servlet4-http2)
* [kafka example](kafka-example)
* [mongodb demo](mongodb-demo)
* [RxJava demo](rxjava-demo)
* [simple JNI demo](simple-jni-demo)
* [JPMS demo](simple-module-example)
* [spring demos](https://github.com/jveverka/spring-examples)
* [ssh server demo](ssh-server-demo)
* [JCE demo](jce-demo)
* [com.fasterxml.jackson](jackson-fasterxml-demo)

### New Features of JDK11
* JDK9  [Feature list](https://openjdk.java.net/projects/jdk9/)
* JDK10 [Feature list](https://openjdk.java.net/projects/jdk/10/)
* JDK11 [Feature list](https://openjdk.java.net/projects/jdk/11/) 

### Features beyond JDK11
* JDK12 [Feature list](https://openjdk.java.net/projects/jdk/12/)
* JDK13 [Feature list](https://openjdk.java.net/projects/jdk/13/)
* JDK14 [Feature list](https://openjdk.java.net/projects/jdk/14/)
* JDK15 [Feature list](https://openjdk.java.net/projects/jdk/15/)
* JDK16 [Feature list](https://openjdk.java.net/projects/jdk/16/)

_Enjoy !_
