# Java - Client x Server communication using websockets

The development is done using java 17.0.13 2024-10-15 LTS
## Install Java
The specific version of java used for the development comes with the following Oracle JDK: https://www.oracle.com/java/technologies/javase/jdk17-0-13-later-archive-downloads.html
Follow along the installation steps provided by Oracle.

## Local Development and Server Setup
1. Clone this repository:
   ```bash
   git clone https://github.com/ameya-keskar/java-websocket-assignment.git
   ```
2. Make sure you have Java installed on your machine. Else read the instructions provided in the previous section.
3. Navigate to the project directory:
   ```bash
   cd java-websocket-assignment
   ```
4. Open up 3 terminal sessions:
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
  Kindly follow the order - servers have to be setup first before the client can communicate with them.
