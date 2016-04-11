package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.model.WalletsId;
import com.futureprocessing.webtask.exchangeoffice.repository.WalletsRepository;

@Service("walletService")
@Transactional
public class WalletServiceImpl implements WalletService {

    private final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

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
    public void buyCurrency(String username, Currency currency) {
        logger.debug(username + " is buying " + (currency.getAmount() * currency.getUnit()) + " " + currency.getCode());

        Wallets requestedWalletEntry = walletsRepository.findOne(new WalletsId("bank", currency.getCode()));
        int amountInBank = requestedWalletEntry.getAmount();

        logger.debug("    Amount of " + currency.getCode() + " in bank before transaction: " + amountInBank);

        if (amountInBank <= 0 || amountInBank < currency.getAmount()) {
            logger.error("    Can't buy!");
            return;
        }

        requestedWalletEntry.setAmount(amountInBank - currency.getAmount());
        walletsRepository.save(requestedWalletEntry);

        requestedWalletEntry = walletsRepository.findOne(new WalletsId("bank", currency.getCode()));
        amountInBank = requestedWalletEntry.getAmount();
        logger.debug("    Amount of " + currency.getCode() + " in bank after transaction: " + amountInBank);
        
        Wallets userWalletEntry = walletsRepository.findOne(new WalletsId(username, currency.getCode()));
        int amountInUser = userWalletEntry.getAmount();
        
        logger.debug("    Amount of " + currency.getCode() + " in user's wallet before transaction: " + amountInUser);
        
        userWalletEntry.setAmount(amountInUser + currency.getAmount());
        walletsRepository.save(userWalletEntry);

        userWalletEntry = walletsRepository.findOne(new WalletsId(username, currency.getCode()));
        amountInUser = userWalletEntry.getAmount();
        logger.debug("    Amount of " + currency.getCode() + " in user's wallet after transaction: " + amountInUser);
    }

    @Override
    public void sellCurrency(String username, Currency currency) {
        logger.debug(username + " is selling " + currency.getAmount() + " " + currency.getCode());
    }

}
