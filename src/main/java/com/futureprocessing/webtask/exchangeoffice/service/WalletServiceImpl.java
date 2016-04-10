package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futureprocessing.webtask.exchangeoffice.dao.WalletsDao;
import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;

@Service("walletService")
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletsDao walletsDao;

    @Override
    public List<Wallets> loadWallet(String username) {
        List<Wallets> wallet = walletsDao.loadWallet(username);
        return wallet;
    }

    @Override
    public List<Wallets> loadWallet(String username, List<Currency> currencies) {
        List<Wallets> wallet = walletsDao.loadWallet(username);
        for (Wallets entry : wallet) {
            Currency currency = currencies
                    .stream()
                    .filter(c -> c.getCode().equals(entry.getCurrency()))
                    .findFirst()
                    .get();
            entry.setUnit(currency.getUnit());
            entry.setUnitPrice(currency.getSellPrice());
            entry.setValue(entry.getUnitPrice() * entry.getAmount() / entry.getUnit());
        }
        return wallet;
    }

    @Override
    public double countSumValue(List<Wallets> wallet) {
        double valueSum = wallet
                .stream()
                .mapToDouble(w -> w.getValue())
                .sum();
        return valueSum;
    }

    @Override
    public List<Wallets> initWallet(String username, List<Currency> currencies) {
        List<Wallets> wallet = new ArrayList<>();

        for (Currency currency : currencies) {
            Wallets entry = new Wallets();
            entry.setUsername(username);
            entry.setCurrency(currency.getCode());
            entry.setAmount(0);
            wallet.add(entry);
        }

        return wallet;
    }

    @Override
    public void saveWallet(String username, Wallets wallet) {
        wallet.setUsername(username);
        walletsDao.addToWallet(wallet);
    }

    @Override
    public void deleteFromWallet(String username, String currency) {
        walletsDao.deleteFromWallet(username, currency);
    }

    @Override
    public Wallets findWalletEntry(String username, String currency) {
        return walletsDao.findWalletEntry(username, currency);
    }

}
