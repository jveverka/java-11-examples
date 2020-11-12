# MongoDB demo
Simple MongoDB / Java demo 

### Quick install MongoDB on localhost
Download MongoDB binary from [here](https://www.mongodb.com/try/download/community). 
Java documentation is [here](https://mongodb.github.io/mongo-java-driver/).
- unpack mongo tar package  
  ```tar xzvf mongodb-linux-x86_64-ubuntu2004-4.4.1.tgz```
- make database directory  
  ```mkdir -p mongodb-linux-x86_64-ubuntu2004-4.4.1/data/db```  
- run mongodb server  
  ```
  cd mongodb-linux-x86_64-ubuntu2004-4.4.1/bin
  ./mongod --dbpath ../data/db
  ```
- create database and user  
  ```
  cd mongodb-linux-x86_64-ubuntu2004-4.4.1/bin
  ./mongo
  use testdb
  db.createUser({user: "testuser", pwd: "secret", roles: [ "readWrite", "dbAdmin" ]}) 
  ```
- setup is complete. next time start mongodb with command  
  ```./mongod --dbpath ../data/db```  

### Build and run
Just run Main in the project or see unit tests. Database server has to be started first.
```
gradle clean build distZip
```  

### Start integration tests
1. Start mongo DB in new terminal window.
```
./mongod --dbpath ../data/db
```
2. Start integration tests.
```
gradle clean test -Dtest.profile=integration
```

### Useful mongodb queries
```
cd mongodb-linux-x86_64-ubuntu2004-4.4.1/bin
./mongo
use testdb
db.roles.find()
db.roles.insert({"_id": "1", "description": "aaa" })
db.roles.remove({ "_id": "1" }, { justOne: true})
db.roles.drop()
```

### References  
* [mongojack](https://mongojack.org/)