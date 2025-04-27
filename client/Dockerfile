FROM openjdk:17-jdk-slim
WORKDIR /app
COPY Client.java /app/
RUN javac Client.java
CMD java Client $SERVER_ADDRESS_1 $PORT_1 $SERVER_ADDRESS_2 $PORT_2 # Default command to run the server; parameters are passed via environment variables
