cd config-server
call docker build -t config-server . 
cd ..\discovery-server
call docker build -t discovery-server .
cd ..\gateway
call docker build -t gateway . 
cd ..