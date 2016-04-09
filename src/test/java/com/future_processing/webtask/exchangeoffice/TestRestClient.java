package com.future_processing.webtask.exchangeoffice;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.future_processing.webtask.exchangeoffice.model.Currencies;
import com.future_processing.webtask.exchangeoffice.service.ExchangeRateService;
import com.future_processing.webtask.exchangeoffice.service.ExchangeRateServiceImpl;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestRestClient {

    @Autowired
    ExchangeRateService exchangeRateService;

    public static void main(String args[]) {
        TestRestClient test = new TestRestClient();
        test.testGetAllEmployee();
    }

    private void testGetAllEmployee() {
        ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl();

        Currencies currencies = exchangeRateService.getExchangeRate();
        System.out.println(currencies.getPublicationDate());
    }

}