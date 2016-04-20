package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;

@Service("walletService")
public class WalletServiceImpl implements WalletService {

    @Override
    public List<Wallets> loadWallet(String username) {
        return loadWalletWithPrices(username, null);
    }

    @Override
    public List<Wallets> loadWalletWithPrices(String username, List<Currency> currencies) {
        List<Wallets> wallet = new ArrayList<>();
        // TODO
        return wallet;
    }

    @Override
    public void saveWallet(String username, Wallets wallet) {
        // TODO
    }

    @Override
    public void deleteFromWallet(String username, String currency) {
        // TODO
    }

    @Override
    public Wallets findWalletEntry(String username, String currency) {
        Wallets wallets = new Wallets();
        // TODO
        return wallets;
    }

    @Override
    public void buyCurrency(String username, Currency currency) throws Exception {
        // TODO
    }

    @Override
    public void sellCurrency(String username, Currency currency) throws Exception {
        // TODO
    }

}
