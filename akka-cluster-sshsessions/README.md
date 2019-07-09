# Akka cluster demo
This is the demo of clustered service which provides 
ssh connection to clients and runs in [akka](https://akka.io/) cluster.

![00](docs/00-architecture.svg)

## Message flows
![00](docs/00-member-joined-flow.svg)
![00](docs/00-member-left-flow.svg)
![01](docs/01-create-session-flow.svg)
![02](docs/02-close-session-flow.svg)
![03](docs/03-error-session-flow.svg)
![04](docs/04-data-session-flow.svg)

### Build and Install
```
gradle clean test installDist
```

### Run cluster or standalone
Run application as 3-node cluster.
```
gradle clean installDist
./docs/bin/start-node-01.sh
./docs/bin/start-node-02.sh
./docs/bin/start-node-03.sh
```
Run 3 node cluster in one terminal window (requires tmux)
```
./docs/bin/start-cluster.sh
```
Run standalone node
```
./docs/bin/start-single-node.sh
```
