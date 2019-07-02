# Kafka Producer / Consumer example

![architecture](docs/architecture-01.svg)

## Setup Kafka 
Download [kafka build](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.2.0/kafka_2.12-2.2.0.tgz) and extract it.
```
tar -xzf kafka_2.12-2.2.0.tgz
cd kafka_2.12-2.2.0
```
Start zookeeper and kafka.
```
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```
Create single topic 'test'.
```
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic service-requests
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic service-responses
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
``` 

## Compile and run
```
gradle clean build
```
TBD