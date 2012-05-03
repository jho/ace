#!/bin/sh

args="-Ddemo.log4j.propertie $@"
mvn -e -q -o compile exec:java -Dexec.mainClass="org.jho.ace.Demo" -Dexec.args="$args" -Dexec.classpathScope=runtime 
