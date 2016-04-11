package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.model.WalletsId;
import com.futureprocessing.webtask.exchangeoffice.repository.WalletsRepository;

@Service("walletService")
@Transactional
@PropertySource(value = "classpath:application.default.properties")
public class WalletServiceImpl implements WalletService {

    private final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    WalletsRepository walletsRepository;

    @Override
    public List<Wallets> loadWallet(String username) {
        return loadWalletWithPrices(username, null);
    }

    @Override
    public List<Wallets> loadWalletWithPrices(String username, List<Currency> currencies) {
        List<Wallets> wallet = walletsRepository.findByUsername(username);

        for (Wallets entry : wallet) {
            Currency currency;
            if (currencies != null) {
                currency = currencies.stream()
                        .filter(c -> c.getCode().equals(entry.getCurrency()))
                        .findFirst().get();
            } else {
                currency = new Currency();
            }
            entry.setUnit(currency.getUnit() != null ? currency.getUnit() : 1);
            entry.setUnitPrice(currency.getSellPrice() != null ? currency.getSellPrice() : 0.0);
            entry.setValue(entry.getUnitPrice() * entry.getAmount() / entry.getUnit());
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
        Wallets wallets = walletsRepository.findOne(new WalletsId(username, currency));

        if (wallets == null) {
            wallets = new Wallets();
            wallets.setUsername(username);
            wallets.setCurrency(currency);
            wallets.setAmount(0);
        }

        return wallets;
    }

    @Override
    public void buyCurrency(String username, Currency currency) {
        String bank = environment.getRequiredProperty("default.bank.username");

        logger.debug(username + " is buying " + currency.getAmount() + " " + currency.getCode());

        Wallets requestedWalletEntry = findWalletEntry(bank, currency.getCode());
        int amountInBank = requestedWalletEntry.getAmount();
        logger.debug("    Amount of " + currency.getCode() + " in bank before transaction: " + amountInBank);

        if (amountInBank <= 0 || amountInBank < currency.getAmount()) {
            logger.error("    Can't buy!");
            return;
        }

        requestedWalletEntry.setAmount(amountInBank - currency.getAmount());
        walletsRepository.save(requestedWalletEntry);

        requestedWalletEntry = findWalletEntry(bank, currency.getCode());
        amountInBank = requestedWalletEntry.getAmount();
        logger.debug("    Amount of " + currency.getCode() + " in bank after transaction: " + amountInBank);

        Wallets userWalletEntry = findWalletEntry(username, currency.getCode());
        int amountInUser = userWalletEntry.getAmount();
        logger.debug("    Amount of " + currency.getCode() + " in user's wallet before transaction: " + amountInUser);

        userWalletEntry.setAmount(amountInUser + currency.getAmount());
        walletsRepository.save(userWalletEntry);

        userWalletEntry = findWalletEntry(username, currency.getCode());
        amountInUser = userWalletEntry.getAmount();
        logger.debug("    Amount of " + currency.getCode() + " in user's wallet after transaction: " + amountInUser);
    }

    @Override
    public void sellCurrency(String username, Currency currency) {
        logger.debug(username + " is selling " + currency.getAmount() + " " + currency.getCode());
    }

}
