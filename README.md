# Java - Client x Server communication using websockets
![Java](https://img.shields.io/static/v1?label=Java&message=17.0.13%20LTS&color=007396&logo=java&logoColor=white)

The development is done using java 17.0.13 2024-10-15 LTS
## Install Java
The specific version of java used for the development comes with the following Oracle JDK: https://www.oracle.com/java/technologies/javase/jdk17-0-13-later-archive-downloads.html
Follow along the installation steps provided by Oracle.

## Local Development and Server Setup

Pre-requisite: Needs Java installed on your machine.

1. Clone this repository:
   ```bash
   git clone https://github.com/ameya-keskar/java-websocket-assignment.git
   ```
2. Navigate to the project directory:
   ```bash
   cd java-websocket-assignment
   ```
3. Open up 3 terminal sessions:
   - Terminal 1:
     ```bash
     cd server
     java Server.java <port-number-1> <file-path-dracula>
     ```
   - Terminal 2:
     ```bash
     cd server
     java Server.java <port-number-2> <file-path-frankenstein>
     ```
   - Terminal 3:
     ```bash
     cd client
     java Client.java localhost <port-number-1> localhost <port-number-2>
     ```
  Kindly follow the order for setting up all the servers - servers have to be setup first before the client can communicate with them.

## Setup on Container Platform - Docker

Pre-requisite: Needs Docker Desktop or Docker daemon installed on your machine.

1. Pull the docker image from docker-hub:
   ```bash
   docker pull dopaminefl00d/java-websocket-assignment-server:v0.1
   docker pull dopaminefl00d/java-websocket-assignment-client:v0.1
   ```
2. Create a Docker volume to store the .txt file(s) to be served by the server:
  ```bash
  docker volume create java-websocket-assignment-files
  ```
3. Push files from your local filesystem current working directory `pwd` to the Docker volume by running a temp. busybox container:
  ```bash
  docker run --rm -v java-websocket-assignment-files:/data -v "$(pwd):/source" busybox cp /source/frankenstein.txt /data/
  docker run --rm -v java-websocket-assignment-files:/data -v "$(pwd):/source" busybox cp /source/dracula.txt /data/
  ```
4. Create a network interface on Docker to facilitate the socket connection between client and servers:
  ```bash
  docker network create java-websocket-assignment-network
  ```
5. Create the containers for the server:
  ```bash
  docker run -d -v java-websocket-assignment-files:/data/  -e PORT=8080 -e FILE_PATH=/data/frankenstein.txt --network java-websocket-assignment-network --name java-server-1 dopaminefl00d/java-websocket-assignment-server:v0.1

  docker run -d -v java-websocket-assignment-files:/data/  -e PORT=8081 -e FILE_PATH=/data/dracula.txt --network java-websocket-assignment-network --name java-server-2 dopaminefl00d/java-websocket-assignment-server:v0.1
  ```

  here,
  `-d`: run the containers in detached mode
  `-v`: mount the volume to the container
  `-e` to define environment variables
  `--network`: specifies network interface, where the server's port will be bound to start listening to traffic
  two containers should start running from this, with names `java-server-1` and `java-server-2`.

6. Create a container for the client:
  ```bash
    docker run -d -e SERVER_ADDRESS_1=java-server-1 -e SERVER_ADDRESS_2=java-server-2 -e PORT_1=8080 -e PORT_2=8081 --network java-websocket-assignment-network --name java-client dopaminefl00d/java-websocket-assignment-client:v0.1
  ```
  to check the output of the client, run:
  ```bash
    docker logs java-client
  ```
