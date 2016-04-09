package com.futureprocessing.webtask.exchangeoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.futureprocessing.webtask.exchangeoffice.model.Currencies;

@Service("exchangeRateService")
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    RestTemplate restTemplate;

    public Currencies getExchangeRate() {
        return restTemplate.getForObject("http://webtask.future-processing.com:8068/currencies", Currencies.class);
    }

}
