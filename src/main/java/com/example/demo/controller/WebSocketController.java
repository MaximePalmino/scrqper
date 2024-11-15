package com.example.demo.controller;

import com.example.demo.entity.Currencies;
import com.example.demo.listener.CurrencyEventListener;
import com.example.demo.service.CurrencyService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {

    private final CurrencyService currencyService;

    public WebSocketController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @MessageMapping("/currency")
    @SendTo("/topic/currency")
    public List<Currencies> sendCurrency(Currencies currency) {
        return currencyService.notifyCurrencyChange(currency);
    }
}
