package com.futureprocessing.webtask.exchangeoffice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.service.WalletService;

@RestController
@ComponentScan
public class WalletController {

    @Autowired
    private WalletService walletService;

    @RequestMapping(value = { "/wallet/save/{username}" }, method = RequestMethod.POST)
    public void saveWalletEntry(@PathVariable("username") String username, @Valid Wallets newWallet) {
        walletService.saveWallet(username, newWallet);
    }

    @RequestMapping(value = "/wallet/delete/{username}/{currency}", method = RequestMethod.DELETE)
    public void operationsOnWalletEntry(@PathVariable("username") String username, @PathVariable("currency") String currency) {
        walletService.deleteFromWallet(username, currency);
    }

    @RequestMapping(value = { "/wallet/buy/{username}" }, method = RequestMethod.POST)
    public void buyWalletEntry(@PathVariable("username") String username, @Valid Currency currency) {
        try {
            currency.setAmount(currency.getAmount() * currency.getUnit());
            walletService.buyCurrency(username, currency);
        } catch (Exception e) {
        }
    }

    @RequestMapping(value = { "/wallet/sell/{username}" }, method = RequestMethod.POST)
    public void sellWalletEntry(@PathVariable("username") String username, @Valid Currency currency) {
        try {
            walletService.sellCurrency(username, currency);
        } catch (Exception e) {
        }
    }

}
