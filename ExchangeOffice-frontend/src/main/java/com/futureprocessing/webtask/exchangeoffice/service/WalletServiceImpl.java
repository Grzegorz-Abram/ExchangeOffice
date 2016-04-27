package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;

@Service("walletService")
public class WalletServiceImpl implements WalletService {

    public List<Wallets> loadWallet(String username) {
        return loadWalletWithPrices(username, null);
    }

    public List<Wallets> loadWalletWithPrices(String username, List<Currency> currencies) {
        List<Wallets> wallet = new ArrayList<>();
        // TODO
        return wallet;
    }

    public void saveWallet(String username, Wallets wallet) {
        // TODO
    }

    public void deleteFromWallet(String username, String currency) {
        // TODO
    }

    public Wallets findWalletEntry(String username, String currency) {
        Wallets wallets = new Wallets();
        // TODO
        return wallets;
    }

    public void buyCurrency(String username, Currency currency) throws Exception {
        // TODO
    }

    public void sellCurrency(String username, Currency currency) throws Exception {
        // TODO
    }

    @Override
    public List<Wallets> findByUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Wallets findByUsernameAndCurrency(String username, String currency) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveWallet(Wallets walletEntry) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteWalletByUsernameAndCurrency(String username, String currency) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteAllWalletEntries(String username) {
        // TODO Auto-generated method stub
        
    }

}
