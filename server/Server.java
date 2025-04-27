import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int portNumber;
    private final String filePath;

    /**
     * Constructor to initialize the server with a port and file path.
     * @param port Port number for the server
     * @param filePath Path to the file to be served
     */
    public Server(int portNumber, String filePath) {
        this.portNumber = portNumber;
        this.filePath = filePath;
    }

    /**
     * Method to start the server and listen for incoming connections.
     * Also streams file contents to the client.
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server started on port " + portNumber);
            while (true) {
                try (
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader fileReader = new BufferedReader(
                        new FileReader(filePath)
                    );
                    PrintWriter clientWriter = new PrintWriter(
                        clientSocket.getOutputStream(),
                        true
                    );
                ) {
                    long startTime = System.currentTimeMillis();
                    System.out.println(
                        "Client connected " + clientSocket.getInetAddress()
                    );

                    String line;
                    while ((line = fileReader.readLine()) != null) {
                        clientWriter.println(line);
                    }
                    System.out.println("File content sent successfully");
                    //connection time

                    long endTime = System.currentTimeMillis();
                    System.out.println(
                        "Connection time: " + (endTime - startTime) + "ms"
                    );
                } catch (IOException e) {
                    System.err.println(
                        "Error sending file content: " + e.getMessage()
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    /**
     * Main method to start the server
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Server <portNumber> <filePath>");
            System.exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);
        String filePath = args[1];
        new Server(portNumber, filePath).start();
    }
}
