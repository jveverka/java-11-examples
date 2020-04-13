# Simple Spring Boot demo
This is simple spring-boot application demo. 

### Rest Endpoints
* __GET__ http://localhost:8080/data/info
* __POST__ http://localhost:8080/data/message 
  ```
  { "data": "message" }
  ```

* REST endpoint handling generic data payloads  
  __POST__ http://localhost:8080/data/generics
  ```
  {"name":"simpleData","data":{"@class":"itx.examples.springboot.demo.dto.generic.SimpleDataPayload","simpleData":"simple"}}
  ```
  ```
  {"name":"complexData","data":{"@class":"itx.examples.springboot.demo.dto.generic.ComplexDataPayload","complexData":"complex"}}
  ```
* Returns information about incoming HTTP request as JSON response. For testing purposes.    
  __ALL__ http://localhost:8080/data/test 
  
### Swagger API docs
* __GET__ http://localhost:8080/v2/api-docs
* __Swagger2 UI__ - http://localhost:8080/swagger-ui.html

## Static Resources
* __GET__ http://localhost:8080/static/inex.html

### Build and run
```
gradle clean build test
java -jar build/libs/spring-demo-1.0.0-SNAPSHOT.jar
```
