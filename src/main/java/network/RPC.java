package network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import mempool.Mempool;
import transaction.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class RPC {
    private final int port;
    private final Mempool mempool;
    private HttpServer server;

    public RPC(int port, Mempool mempool) {
        this.port = port;
        this.mempool = mempool;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new TransactionHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Transaction RPC Server started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }


    private class TransactionHandler implements HttpHandler {
        private final ObjectMapper objectMapper;

        public TransactionHandler() {
            this.objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Transaction.class, new TransactionDeserializer());
            objectMapper.registerModule(module);
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                StringBuilder sb = new StringBuilder();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    sb.append(new String(buffer, 0, length));
                }

                try {
                    // Convert JSON string to Transaction object
                    Transaction transaction = objectMapper.readValue(sb.toString(), Transaction.class);

                    System.out.println("Sending to Mempool..");
                    boolean added = mempool.addRequest(transaction); // Update this method to accept Transaction object

                    if (added) {
                        System.out.println("transaction successfully added to mempool");
                    } else {
                        System.out.println("invalid transaction");
                    }

                    String response = "Transaction received";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes(StandardCharsets.UTF_8));
                    }

                } catch (Exception e) {
                    String errorResponse = "Error processing transaction: " + e.getMessage();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(400, errorResponse.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(errorResponse.getBytes(StandardCharsets.UTF_8));
                    }
                }

            } else {
                String response = "Only POST method allowed";
                exchange.sendResponseHeaders(405, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }

}
