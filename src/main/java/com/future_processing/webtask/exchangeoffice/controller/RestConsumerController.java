package com.future_processing.webtask.exchangeoffice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.future_processing.webtask.exchangeoffice.model.Currencies;
import com.future_processing.webtask.exchangeoffice.service.ExchangeRateService;

@RestController
public class RestConsumerController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @RequestMapping(value = "/ExchangeRate")
    public Currencies showExchangeRate() {
        return exchangeRateService.getExchangeRate();
    }

}
