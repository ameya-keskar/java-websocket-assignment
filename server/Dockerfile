FROM openjdk:17-jdk-slim
WORKDIR /app
COPY Server.java /app/
RUN javac Server.java # Compile the Java file
ARG PORT=8080 # Exposing a placeholder port; this will be overridden dynamically
EXPOSE $PORT
CMD java Server "$PORT" "$FILE_PATH" # Default command to run the server; parameters are passed via environment variables
