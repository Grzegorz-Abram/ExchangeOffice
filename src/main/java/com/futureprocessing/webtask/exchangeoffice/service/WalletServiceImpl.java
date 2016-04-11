package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.model.WalletsId;
import com.futureprocessing.webtask.exchangeoffice.repository.WalletsRepository;

@Service("walletService")
@Transactional
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletsRepository walletsRepository;

    @Override
    public List<Wallets> loadWallet(String username) {
        List<Wallets> wallet = walletsRepository.findByUsername(username);
        return wallet;
    }

    @Override
    public List<Wallets> loadWallet(String username, List<Currency> currencies) {
        List<Wallets> wallet = walletsRepository.findByUsername(username);
        if (currencies != null) {
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
        }
        return wallet;
    }

    @Override
    public Double countSumValue(List<Wallets> wallet) {
        Double valueSum;
        try {
            valueSum = wallet
                    .stream()
                    .mapToDouble(w -> w.getValue())
                    .sum();
        } catch (Exception e) {
            valueSum = null;
        }
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
        walletsRepository.save(wallet);
    }

    @Override
    public void deleteFromWallet(String username, String currency) {
        walletsRepository.delete(new WalletsId(username, currency));
    }

    @Override
    public Wallets findWalletEntry(String username, String currency) {
        return walletsRepository.findOne(new WalletsId(username, currency));
    }

    @Override
    public void buyCurrency(String name, Currency currency) {
        System.out.println(name + " is buying " + (currency.getAmount() * currency.getUnit()) + " " + currency.getCode());
    }

}
