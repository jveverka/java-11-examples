# Java SSH server + client library and examples
Simple ssh server and client with examples how to use it. Implementation uses [Apache Mina](https://mina.apache.org/) project.
Server supports following clients:
* __ssh terminals__ - like putty or 'OpenSSH SSH client'
* __ssh commands__ - commands send using 'OpenSSH SSH client'
* __java client__ - 'ssh-java' client library for connection from java runtime

### Compile and Run tests
```gradle clean build```

### connect to running server
In this mode, ssh client connects to running server and gets REPL interface.
```
ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null user@localhost -p 2222
ssh user@localhost -p 2222
```
After client is connected, it may send supported commands via terminal. 'exit' command closes the session.

### send command to runing server
In this mode, ssh client will send separate commands to running server.
```
sshpass -p secret ssh user@localhost -p 2222 "set 22"
sshpass -p secret ssh user@localhost -p 2222 "get"
```

### supported commands
* __get__ - will get internal state
* __set &lt;value&gt;__ - will set internal state to given value
