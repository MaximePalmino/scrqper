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
        System.out.println("Currency created: " + currency);
        currencyRepository.save(currency);
        notifyCurrencyChange(currency);
    }

    public void updateCurrency(Currencies currency) {
        System.out.println("Currency updated: " + currency);
        currencyRepository.save(currency);
        notifyCurrencyChange(currency);
    }

    public void deleteCurrency(Currencies currency) {
        System.out.println("Currency deleted: " + currency);
        currencyRepository.delete(currency);
        notifyCurrencyChange(currency);
    }

    public List<Currencies> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public void notifyCurrencyChange(Currencies currency) {
        // Send the updated currency to all connected WebSocket clients
        messagingTemplate.convertAndSend("/topic/currency", currency);
    }
}