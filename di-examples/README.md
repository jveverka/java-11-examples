# Dependency Injection examples

This demo shows basic use cases for some popular Dependency Injection frameworks.
__data-service__ project implements example service beans `MessageService`. There are two implementations of same `MessageService` interface.
1. `itx.examples.di.service.impl.MessageServiceSyncImpl` - synchronous service
2. `itx.examples.di.service.impl.MessageServiceAsyncImpl` - asynchronous service

data-service implementation requires only Java SE runtime and has no framework dependencies. 
`MessageService` is used in application examples for DI showcases.

__app-simple__ - is plain Java SE application using `MessageService` without any DI framework.

## Dependency Injection providers
Those projects are providers for various popular DI frameworks.
* __di-spring__ - dependency injection for [spring.io](https://spring.io/)
* __di-guice__ - dependency injection for [google guice](https://github.com/google/guice)
* __di-dagger__ - dependency injection for [google dagger](https://github.com/google/dagger)

## Example Applications (DI consumers)
* __app-spring__ - example application using `MessageService` instances with spring DI system. 
* __app-guice__ - example application using `MessageService` instances with google guice DI system.
* __app-dagger__ - example application using `MessageService` instances with google dagger DI system.

### Build and Test
```
gradle clean build
```
