package com.futureprocessing.webtask.exchangeoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.futureprocessing.webtask.exchangeoffice.model.Currencies;

@Service("exchangeRateService")
@PropertySource(value = "classpath:application.default.properties")
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    RestTemplate restTemplate;

    private String url;

    @Autowired
    public ExchangeRateServiceImpl(Environment environment) {
        this.url = environment.getRequiredProperty("default.exchangeRateService.url");
    }

    @Cacheable("currencies")
    public Currencies getExchangeRate() {
        return restTemplate.getForObject(url, Currencies.class);
    }

}
