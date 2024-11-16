package com.example.demo.service;

import com.example.demo.entity.Currencies;
import com.example.demo.repository.CurrenciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrenciesRepository currencyRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CurrencyService(CurrenciesRepository currencyRepository, SimpMessagingTemplate messagingTemplate) {
        this.currencyRepository = currencyRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void saveCurrency(Currencies currency) {
        currencyRepository.save(currency);
        notifyCurrencyChange("INSERT", currency);
    }

    public void updateCurrency(Currencies currency) {
        currencyRepository.save(currency);
        notifyCurrencyChange("UPDATE", currency);
    }

    public void deleteCurrency(Currencies currency) {
        currencyRepository.delete(currency);
        notifyCurrencyChange("DELETE", currency);
    }

    public List<Currencies> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    private void notifyCurrencyChange(String operation, Currencies currency) {
        // Create a DTO for WebSocket notification
        CurrencyUpdate update = new CurrencyUpdate(
                operation,
                currency.getId(),
                currency.getName(),
                currency.getSymbol(),
                System.currentTimeMillis()
        );
        messagingTemplate.convertAndSend("/topic/currency", update);
    }

    // Inner DTO class for WebSocket notifications
    public static class CurrencyUpdate {
        private String operation;
        private String id;
        private String name;
        private String symbol;
        private long timestamp;

        public CurrencyUpdate(String operation, String id, String name, String symbol, long timestamp) {
            this.operation = operation;
            this.id = id;
            this.name = name;
            this.symbol = symbol;
            this.timestamp = timestamp;
        }

        // Getters and setters
        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
