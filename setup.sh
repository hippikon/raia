#!/bin/sh
export JAVA_HOME="/Users/rmadhuri2021/Documents/digital.pensieve/Madhuri/Tools/jdk-19.0.1.jdk/Contents/Home"
export M2_HOME="Users/rmadhuri2021/Documents/digital.pensieve/Madhuri/Tools/apache-maven-3.8.5"
export PATH=$JAVA_HOME/bin:$PATH
export PATH=$M2_HOME/bin:$PATH
export JAVA_OPTS="$JAVA_OPTS -Xms1024m -Xmx2048m -XX:PermSize=32m"
mvn eclipse:clean -Dwtpversion=2.0
mvn eclipse:eclipse -Dwtpversion=2.0
