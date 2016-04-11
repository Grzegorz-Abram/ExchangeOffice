package com.futureprocessing.webtask.exchangeoffice.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import com.futureprocessing.webtask.exchangeoffice.model.Currencies;
import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.service.ExchangeRateService;

@Controller
@ComponentScan
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    List<Currency> getCurrencies() {
        return getActualExchangeRates().getItems();
    }

    Date getCurrenciesPublicationDate() {
        return getActualExchangeRates().getPublicationDate();
    }

    Currencies getActualExchangeRates() {
        return exchangeRateService.getExchangeRate();
    }

}
