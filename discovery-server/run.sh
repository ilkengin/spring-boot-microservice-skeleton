#!/bin/sh




echo "********************************************************"
echo "Starting discovery-server "
echo "********************************************************"
java   $MEM_ARGS -Dspring.profiles.active=$PROFILE -jar app.jar