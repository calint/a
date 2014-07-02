#!/bin/sh
export JAVA_HOME=/jre&&
export PATH=$JAVA_HOME/bin:$PATH&&
export CLASSPATH=/a/bin&&
cd /a&&sh ba.sh server_port 80

   