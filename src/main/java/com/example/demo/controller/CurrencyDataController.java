package com.example.demo.controller;

import com.example.demo.entity.CurrencyData;
import com.example.demo.service.CurrencyService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CurrencyDataController {

    private final CurrencyService currencyService;

    public CurrencyDataController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @MessageMapping("/currencydata")
    @SendTo("/topic/currencydata")
    public List<CurrencyData> sendCurrency(CurrencyData currencyData) {
        return currencyService.getAllCurrencyData();
    }

    // Optional: You might want to add these REST endpoints for CRUD operations
    @PostMapping("/api/currencydata")
    public void saveCurrencyData(@RequestBody CurrencyData currencyData) {
        currencyService.saveCurrencyData(currencyData);
    }

    @GetMapping("/api/currencydata")
    public List<CurrencyData> getAllCurrencyData() {
        return currencyService.getAllCurrencyData();
    }
}