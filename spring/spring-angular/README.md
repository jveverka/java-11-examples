# Spring Boot & Angular demo
This is simple spring-boot application demo with angular.io UI. 

### Rest Endpoints
GET: http://localhost:8888/services/app/info
```
{"name":"spring-angular","version":"1.0.0","timeStamp":1548350735229}
```

### UI URL
`http://localhost:8888/index.html`

### Build and Run
First run
```
gradle :client:npmFullBuild
```
Common builds
```
gradle clean build
java -jar server/build/libs/server-1.0.0-SNAPSHOT.jar 
```
