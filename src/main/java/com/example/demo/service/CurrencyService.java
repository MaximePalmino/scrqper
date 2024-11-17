package com.example.demo.service;

import com.example.demo.entity.Currencies;
import com.example.demo.entity.CurrencyData;
import com.example.demo.repository.CurrenciesRepository;
import com.example.demo.repository.CurrencyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CurrencyService {

    private final CurrenciesRepository currencyRepository;
    private final CurrencyDataRepository currencyDataRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CurrencyService(
            CurrenciesRepository currencyRepository,
            CurrencyDataRepository currencyDataRepository,
            SimpMessagingTemplate messagingTemplate) {
        this.currencyRepository = currencyRepository;
        this.currencyDataRepository = currencyDataRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // Currency methods
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

    // Currency Data methods
    public void saveCurrencyData(CurrencyData currencyData) {
        currencyDataRepository.save(currencyData);
        notifyCurrencyDataChange("INSERT", currencyData);
    }

    public void updateCurrencyData(CurrencyData currencyData) {
        currencyDataRepository.save(currencyData);
        notifyCurrencyDataChange("UPDATE", currencyData);
    }

    public void deleteCurrencyData(CurrencyData currencyData) {
        currencyDataRepository.delete(currencyData);
        notifyCurrencyDataChange("DELETE", currencyData);
    }

    public List<CurrencyData> getAllCurrencyData() {
        return currencyDataRepository.findAll();
    }

    // Notification methods
    private void notifyCurrencyChange(String operation, Currencies currency) {
        CurrencyUpdate update = new CurrencyUpdate(
                operation,
                currency.getId(),
                currency.getName(),
                currency.getSymbol(),
                System.currentTimeMillis()
        );
        messagingTemplate.convertAndSend("/topic/currency", update);
    }

    private void notifyCurrencyDataChange(String operation, CurrencyData currencyData) {
        CurrencyDataUpdate update = new CurrencyDataUpdate(
                operation,
                currencyData.getSource(),
                currencyData.getUpdatedAt(),
                currencyData.getPrice(),
                currencyData.getMarketCap(),
                currencyData.getTrustFactor(),
                System.currentTimeMillis()
        );
        messagingTemplate.convertAndSend("/topic/currencyData", update);
    }

    // DTO classes
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

        public CurrencyDataUpdate(String operation, String source, Date updatedAt, float price,
                                  float marketCap, float trustFactor, long timestamp) {
            this.operation = operation;
            this.source = source;
            this.updatedAt = updatedAt;
            this.price = price;
            this.marketCap = marketCap;
            this.trustFactor = trustFactor;
            this.timestamp = timestamp;
        }

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