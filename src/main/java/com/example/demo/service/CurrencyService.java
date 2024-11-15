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
    }

    public List<Currencies> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public List<Currencies> notifyCurrencyChange(Currencies currency) {
        List<Currencies> currencies = getAllCurrencies();
        System.out.println(currencies);
        System.out.println("CASE 1 :)");
        return currencies;
//        messagingTemplate.convertAndSend("/topic/currency", currencies);
    }
}
