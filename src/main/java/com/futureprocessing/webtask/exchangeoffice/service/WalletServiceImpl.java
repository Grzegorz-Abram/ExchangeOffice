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
                        .findFirst()
                        .orElse(new Currency());
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
        Wallets cashFromBank = findWalletEntry(environment.getRequiredProperty("default.bank.username"), currency.getCode());

        if (cashFromBank.getAmount() <= 0 || cashFromBank.getAmount() < currency.getAmount()) {
            logger.error("    Can't buy!");
            return;
        }

        cashFromBank.setAmount(cashFromBank.getAmount() - currency.getAmount());
        walletsRepository.save(cashFromBank);

        Wallets cashFromUser = findWalletEntry(username, currency.getCode());
        cashFromUser.setAmount(cashFromUser.getAmount() + currency.getAmount());
        walletsRepository.save(cashFromUser);
    }

    @Override
    public void sellCurrency(String username, Currency currency) {
        Wallets cashFromUser = findWalletEntry(username, currency.getCode());

        if (cashFromUser.getAmount() <= 0 || cashFromUser.getAmount() < currency.getAmount()) {
            logger.error("    Can't sell!");
            return;
        }

        cashFromUser.setAmount(cashFromUser.getAmount() - currency.getAmount());
        walletsRepository.save(cashFromUser);

        Wallets cashFromBank = findWalletEntry(environment.getRequiredProperty("default.bank.username"), currency.getCode());
        cashFromBank.setAmount(cashFromBank.getAmount() + currency.getAmount());
        walletsRepository.save(cashFromBank);
    }

}
