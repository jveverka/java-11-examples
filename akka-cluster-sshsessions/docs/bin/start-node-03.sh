#!/bin/bash
# run 'gradle clean installDist' prior to this script

echo `pwd`
build/install/akka-cluster-sshsessions/bin/akka-cluster-sshsessions -c `pwd`/docs/config/node-03.conf

