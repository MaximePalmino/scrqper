package com.example.demo.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.postgresql.PGConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class DatabaseListenerService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final AtomicBoolean running = new AtomicBoolean(true);
    private Thread listenerThread;

    @PostConstruct
    public void startListening() {
        listenerThread = new Thread(this::listenToCurrencyUpdates);
        listenerThread.setName("pg-currency-listener");
        listenerThread.start();
        System.out.println("Listening");
    }

    @PreDestroy
    public void stopListening() {
        running.set(false);
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

    private void listenToCurrencyUpdates() {
        System.out.println("Listening 2");
        while (running.get()) {
            try (Connection connection = jdbcTemplate.getDataSource().getConnection();
                 Statement statement = connection.createStatement()) {

                // Create the trigger if it doesn't exist
                statement.execute("""
                   DO $$
                           BEGIN
                               IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'currency_changes_trigger') THEN
                                   CREATE TRIGGER currency_changes_trigger
                                   AFTER INSERT OR UPDATE OR DELETE ON currencies
                                   FOR EACH ROW EXECUTE FUNCTION notify_currency_changes();
                               END IF;
                           END $$;
                           
                """);

                statement.execute("LISTEN currency_changes");

                PGConnection pgConnection = connection.unwrap(PGConnection.class);

                while (running.get()) {
                    var notifications = pgConnection.getNotifications(1000); // 1 second timeout
                    if (notifications != null) {
                        for (var notification : notifications) {
                            try {
                                // Parse the JSON payload
                                CurrencyUpdate update = objectMapper.readValue(
                                        notification.getParameter(),
                                        CurrencyUpdate.class
                                );
                                System.out.println(update);

                                // Send to WebSocket subscribers
                                messagingTemplate.convertAndSend("/topic/currency", update);

                            } catch (Exception e) {
                                System.err.println("Error processing notification: " + e.getMessage());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Database listener error: " + e.getMessage());
                // Wait before reconnecting
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // Data class for currency updates
    public static class CurrencyUpdate {
        private String operation;
        private String id;
        private String name;
        private String symbol;
        private long timestamp;

        // Getters and setters
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}