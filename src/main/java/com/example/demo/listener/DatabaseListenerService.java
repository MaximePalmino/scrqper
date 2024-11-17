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
import java.util.Date;
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
        listenerThread = new Thread(this::listenToUpdates);
        listenerThread.setName("pg-database-listener");
        listenerThread.start();
        System.out.println("Database listener started");
    }

    @PreDestroy
    public void stopListening() {
        running.set(false);
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

    private void listenToUpdates() {
        while (running.get()) {
            try (Connection connection = jdbcTemplate.getDataSource().getConnection();
                 Statement statement = connection.createStatement()) {

                // Create triggers if they don't exist
                createTriggers(statement);

                // Listen to both channels
                statement.execute("LISTEN currency_changes");
                statement.execute("LISTEN currency_data_changes");

                PGConnection pgConnection = connection.unwrap(PGConnection.class);

                while (running.get()) {
                    var notifications = pgConnection.getNotifications(1000); // 1 second timeout
                    if (notifications != null) {
                        for (var notification : notifications) {
                            try {
                                processNotification(notification);
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

    private void createTriggers(Statement statement) throws Exception {
        // Currency changes trigger
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

        // Currency data changes trigger
        statement.execute("""
            DO $$
            BEGIN
                IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'currency_data_changes_trigger') THEN
                    CREATE TRIGGER currency_data_changes_trigger
                    AFTER INSERT OR UPDATE OR DELETE ON currency_data
                    FOR EACH ROW EXECUTE FUNCTION notify_currency_data_changes();
                END IF;
            END $$;
        """);
    }

    private void processNotification(org.postgresql.PGNotification notification) throws Exception {
        switch (notification.getName()) {
            case "currency_changes":
                CurrencyUpdate currencyUpdate = objectMapper.readValue(
                        notification.getParameter(),
                        CurrencyUpdate.class
                );
                System.out.println("Currency update: " + currencyUpdate);
                messagingTemplate.convertAndSend("/topic/currency", currencyUpdate);
                break;

            case "currency_data_changes":
                CurrencyDataUpdate currencyDataUpdate = objectMapper.readValue(
                        notification.getParameter(),
                        CurrencyDataUpdate.class
                );
                System.out.println("Currency data update: " + currencyDataUpdate);
                messagingTemplate.convertAndSend("/topic/currencyData", currencyDataUpdate);
                break;
        }
    }

    // Data classes for notifications
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

    public static class CurrencyDataUpdate {
        private String operation;
        private String source;
        private Date updatedAt;
        private float price;
        private float marketCap;
        private float trustFactor;
        private long timestamp;

        // Getters and setters
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public Date getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
        public float getPrice() { return price; }
        public void setPrice(float price) { this.price = price; }
        public float getMarketCap() { return marketCap; }
        public void setMarketCap(float marketCap) { this.marketCap = marketCap; }
        public float getTrustFactor() { return trustFactor; }
        public void setTrustFactor(float trustFactor) { this.trustFactor = trustFactor; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}