package com.example.demo.listener;

import com.example.demo.entity.Currencies;
import com.example.demo.service.CurrencyService;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyEventListener {
    private final CurrencyService currencyService;

    public CurrencyEventListener() {
        this(null);
    }

    @Autowired
    public CurrencyEventListener(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PrePersist
    public void prePersist(Currencies currencies) {
        System.out.println("Currency created: " + currencies);
    }

    @PostUpdate
    public void onPostUpdate(Currencies currency) {
        System.out.println("Currency updated: " + currency);
        if (currencyService != null) {
            currencyService.notifyCurrencyChange(currency);
        }
    }

    @PostRemove
    public void onPostRemove(Currencies currency) {
        System.out.println("Currency deleted: " + currency);
        if (currencyService != null) {
            currencyService.notifyCurrencyChange(currency);
        }
    }
}