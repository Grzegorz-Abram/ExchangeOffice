package com.futureprocessing.webtask.exchangeoffice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.futureprocessing.webtask.exchangeoffice.model.Currencies;

@Service("exchangeRateService")
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);

    @Autowired
    RestTemplate restTemplate;

    public Currencies getExchangeRate() {
        Currencies currencies;
        try {
            currencies = restTemplate.getForObject("http://webtask.future-processing.com:8068/currencies", Currencies.class);
            logger.debug("downloaded actual exchange rates");
        } catch (RestClientException e) {
            currencies = new Currencies();
        }
        return currencies;
    }

}
