# Java SSH server example
Simple ssh server java example.

### connect to running server
In this mode, ssh client connects to running server and gets REPL interface.
```ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null user@localhost -p 2222```
```ssh juraj@localhost -p 2222```
After client is connected, it may send supported commands via terminal. 'exit' command closes the session.

### send command to runing server
In this mode, ssh client will send separate commands to running server.
```sshpass -p secret ssh juraj@localhost -p 2222 "set 22"```
```sshpass -p secret ssh juraj@localhost -p 2222 "get"```

### supported commands
* __get__ - will get internal state
* __set &lt;value&gt;__ - will set internal state to given value
