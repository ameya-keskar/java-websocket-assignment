import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class Client {

    private final List<String> serverIPAddresses;
    private final List<Integer> serverPortNumbers;

    /**
     * Constructs a new Client instance with the specified server IP addresses and port numbers.
     *
     * @param serverIPAddresses the list of server IP addresses
     * @param serverPortNumbers the list of server port numbers
     */
    public Client(
        List<String> serverIPAddresses,
        List<Integer> serverPortNumbers
    ) {
        this.serverIPAddresses = serverIPAddresses;
        this.serverPortNumbers = serverPortNumbers;
    }

    public Map<String, Integer> fetchWordCounts()
        throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(
            serverIPAddresses.size()
        );
        List<Future<Map<String, Integer>>> futures = new ArrayList<>();

        // Submit tasks for fetching and processing data from each server
        for (int i = 0; i < serverIPAddresses.size(); i++) {
            String address = serverIPAddresses.get(i);
            int port = serverPortNumbers.get(i);
            futures.add(
                executorService.submit(() -> fetchWordsFromServer(address, port)
                )
            );
        }

        // Aggregate word counts from all servers
        Map<String, Integer> totalWordCount = new HashMap<>();
        for (Future<Map<String, Integer>> future : futures) {
            Map<String, Integer> wordCount = future.get();
            wordCount.forEach((word, count) ->
                totalWordCount.merge(word, count, Integer::sum)
            );
        }

        executorService.shutdown();
        return totalWordCount;
    }

    /**
     * Fetches file content from a specific server and computes word frequencies.
     * @param address Server address
     * @param port Server port
     * @return A map of words to their counts
     */
    private Map<String, Integer> fetchWordsFromServer(
        String address,
        int port
    ) {
        Map<String, Integer> wordCount = new HashMap<>();
        Pattern wordPattern = Pattern.compile("\\b\\w+\\b"); // Regex to match words

        try (
            Socket socket = new Socket(address, port);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            )
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordPattern
                    .matcher(line.toLowerCase())
                    .results()
                    .map(match -> match.group())
                    .forEach(word -> wordCount.merge(word, 1, Integer::sum));
            }
        } catch (IOException e) {
            System.err.println(
                "Error connecting to server " +
                address +
                ":" +
                port +
                " - " +
                e.getMessage()
            );
        }
        return wordCount;
    }

    /**
     * Method to start the client server and fetch lines of texts from the multiple servers, concurrently
     */
    public static void main(String[] args)
        throws InterruptedException, ExecutionException {
        if (args.length != 4) {
            System.out.println(
                "Usage: java Client <server1_address> <server1_port> <server2_address> <server2_port>"
            );
            return;
        }

        List<String> serverIPAddresses = Arrays.asList(args[0], args[2]);
        List<Integer> serverPortNumbers = Arrays.asList(
            Integer.parseInt(args[1]),
            Integer.parseInt(args[3])
        );
        Client client = new Client(serverIPAddresses, serverPortNumbers);
        long startTime = System.currentTimeMillis();
        Map<String, Integer> wordCounts = client.fetchWordCounts();
        long connectionTime = System.currentTimeMillis() - startTime;

        wordCounts
            .entrySet()
            .stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sort by count in descending order
            .limit(5) // Limit to top 5 words
            .forEach(entry ->
                System.out.println(entry.getKey() + ": " + entry.getValue())
            );
        long processingTime =
            System.currentTimeMillis() - startTime - connectionTime;
        System.out.println("Connection time: " + connectionTime + " ms");
        System.out.println("Processing time: " + processingTime + " ms");
    }
}
