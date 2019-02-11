# Simple SpringBoot security demo

Really simple spring security demo. Public data is accessible without login.
Private data is accessible only after login. Session timeout is set to 5 minutes.
After login, each request must use same cookie JSESSIONID, because server is tracking http sessions by this cookie.

### Login
* __POST__ http://localhost:8888/services/security/login
```
{
	"userName": "joe",
	"password": "secret"
}
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