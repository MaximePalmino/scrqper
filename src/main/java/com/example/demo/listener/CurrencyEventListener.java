package com.example.demo.listener;

import com.example.demo.entity.Currencies;
import com.example.demo.service.CurrencyService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyEventListener {

    private CurrencyService currencyService;

    // No-argument constructor
    public CurrencyEventListener() {
    }

    // Setter method for dependency injection
    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PrePersist
    public void prePersist(Currencies currencies) {
        System.out.println(currencies);
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    public void onPostUpdate(Currencies currency) {
        System.out.println(currency);
        System.out.println("Hello World");
        if (currencyService != null) {
            currencyService.notifyCurrencyChange(currency);
        }
    }
}
