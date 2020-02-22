# Simple SpringBoot security JWT demo

This project is __WIP__

[JWT for Java](https://github.com/jwtk/jjwt)

### Login
* __POST__ http://localhost:8888/services/security/login
  ```
  {
     "userName": "jane",
     "password": "secret"
  }
  ```
  In case login is successful, user data is returned in response.
  ```
  {
      "userId": {
          "id": "joe"
      },
      "roles": [
          { "id": "ROLE_USER" }, { "id": "ROLE_ADMIN" }
      ],
      "jwToken": {
          "token": <token>
      }
  }
  ```
  jwToken string must be used in http header for each request
  ```
  Authorization: Bearer <token>
  ```
  
### Logout
* __GET__ http://localhost:8888/services/security/logout

### Users, Passwords and Roles
* joe / secret, ROLE_USER
* jane / secret, ROLE_USER, ROLE_ADMIN
* alice / secret, ROLE_PUBLIC

### Get protected data
GET protected data for different user roles:
* __GET__ http://localhost:8888/services/data/users/all (ROLE_USER, ROLE_ADMIN)
* __GET__ http://localhost:8888/services/data/admins/all (ROLE_ADMIN)

### Get public data
* __GET__ http://localhost:8888/services/public/data/all

### Build and run
```
gradle clean build 
java -jar build/libs/spring-security-0.0.1-SNAPSHOT.jar 
```