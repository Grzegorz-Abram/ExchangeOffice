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
    public List<Wallets> loadWallet(String username, List<Currency> currencies) {
        List<Wallets> wallet = walletsDao.loadWallet(username);
        for (Wallets entry : wallet) {
            float sellPrice = currencies
                .stream()
                .filter(c -> c.getCode().equals(entry.getCurrency()))
                .findFirst()
                .get()
                .getSellPrice();
            entry.setUnitPrice(sellPrice);
            entry.setValue(entry.getUnitPrice() * entry.getAmount());
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

        for (Currency entry : currencies) {
            Wallets w = new Wallets();
            w.setUsername(username);
            w.setCurrency(entry.getCode());
            w.setAmount(0);
            wallet.add(w);
        }

        return wallet;
    }

    @Override
    public void saveWallet(Wallets wallet) {
        walletsDao.addToWallet(wallet);
    }

}
