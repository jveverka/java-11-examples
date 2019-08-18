# Java SSH server + client library and examples
Simple ssh server and client with examples how to use it. Implementation uses [Apache Mina](https://mina.apache.org/) project.
Server supports following clients:
* __ssh terminals__ - like putty or 'OpenSSH SSH client', providing [REPL](https://en.wikipedia.org/wiki/Read%E2%80%93eval%E2%80%93print_loop) interface.
* __ssh commands__ - single commands send using 'OpenSSH SSH client'
* __java client__ - 'ssh-java' client library for connection from java runtime

![Supported use cases](docs/supported-use-cases.svg)

## Components
* __ssh-server__ - versatile library for embedding ssh server into Java applications.
* __ssh-client__ - versatile library for embedding ssh client into Java applications.
* __ssh-examples__ - examples of use for ssh-server and ssh-client.

### Using ssh-server
* use dependencies
  ```
  <dependency>
    <groupId>itx.ssh.server</groupId>
    <artifactId>ssh-server</artifactId>
    <version>1.0.0</version>
  </dependency>
  ```
  ```
  compile 'itx.ssh.server:ssh-server:1.0.0'
  ```
* start server
* override __CommandProcessors__

### Using ssh-client
* use dependencies
  ```
  <dependency>
    <groupId>itx.ssh.client</groupId>
    <artifactId>ssh-client</artifactId>
    <version>1.0.0</version>
  </dependency>
  ```
  ```
  compile 'itx.ssh.client:ssh-client:1.0.0'
  ```
* start client
* send and receive messages


### Compile and Run tests
This command will compile project, run the tests, localy publish maven artifacts and create ssh-examples distribution.
```
gradle clean build publishToMavenLocal installDist distZip
```
After build is done, server can be started from commandline, listening on port 2222.
```
./ssh-examples/build/install/ssh-examples/bin/ssh-examples
```

### 1. connect to running server
In this mode, ssh client connects to running server and gets REPL interface.
Example application implements __supported commands__ for reference.
```
ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null user@localhost -p 2222
ssh user@localhost -p 2222
```
After client is connected, it may send supported commands via terminal. 'exit' command closes the session.

### 2. send single command to runing server
In this mode, ssh client will send separate commands to running server.
Example application implements __supported commands__ for reference.
```
sshpass -p secret ssh user@localhost -p 2222 "set 22"
sshpass -p secret ssh user@localhost -p 2222 "get"
```

### 3. use ssh-client to interconnect 2 JVMs over ssh
In this mode, ssh-client library may be used to conect to runnig instance of ssh-server.

#### supported commands
* __get__ - will get internal state
* __set &lt;value&gt;__ - will set internal state to given value
