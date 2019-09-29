# Simple ElasticSearch demo
This demo uses [ElasticSearch](https://www.elastic.co/downloads/elasticsearch) as backend database.
Service [DataService](src/main/java/itx/elastic/demo/DataService.java) implements CRUD access to documents in index.

## Compile and Run
```
gradle clean build test
```

## Install ElasticSearch and Kibana
See [instructions](docs/es-install.md) how to install [ElasticSearch and Kibana](https://www.elastic.co/products/elastic-stack) on localhost.
The [cheat-sheet](docs/es-cheat-sheet.md) describes most used commands.

## Run integration tests
This project contains integration unit tests. An ElasticSearch instance is required to run on localhost before running integration tests.
```
gradle clean build test -Dtest.profile=integration
```
| ES parameter |       Value |
|--------------|-------------|
| address      | "127.0.0.1" | 
| port         | 9200        |
| scheme       | "http"      |

