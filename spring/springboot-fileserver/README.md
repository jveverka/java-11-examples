# Simple FileServer demo
This is simple spring-boot FileServer demo. This server makes specified directory accessible via REST APIs.

### Rest Endpoints
* __GET__ http://localhost:8080/services/files/list/** - list content directory or subdirectory
* __GET__ http://localhost:8080/services/files/download/** - download file on path 


### Build and run
Variable ``file.server.home`` in ``application.properties`` file defines root directory to be exposed via REST APIs.
```
gradle clean build
java -jar build/libs/springboot-fileserver-0.0.1-SNAPSHOT.jar --spring.config.location=file:./src/main/resources/application.properties
```
