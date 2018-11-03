# Simple Spring Boot 2.0 demo
This is simple spring-boot application demo. 

### Rest Endpoints
* __GET__ http://localhost:8080/data/info
* __POST__ http://localhost:8080/data/message 
```
{ "data": "message" }
```

* __POST__ http://localhost:8080/data/generics - REST endpoint handling generic data payloads
```
{"name":"simpleData","data":{"@class":"itx.examples.springboot.demo.dto.generic.SimpleDataPayload","simpleData":"simple"}}
```
```
{"name":"complexData","data":{"@class":"itx.examples.springboot.demo.dto.generic.ComplexDataPayload","complexData":"complex"}}
```
### Swagger API docs
* __GET__ http://localhost:8080/v2/api-docs

### Build and run
```
gradle clean build
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```
