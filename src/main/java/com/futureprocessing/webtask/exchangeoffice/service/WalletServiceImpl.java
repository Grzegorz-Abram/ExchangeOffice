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

    @Autowired
    private ExchangeRateService exchangeRateService;

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
            entry.setUnitPrice(currency.getPurchasePrice() != null ? currency.getPurchasePrice() : 0.0);
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
            wallets.setAmount(0.0);
        }

        return wallets;
    }

    @Override
    public void buyCurrency(String username, Currency currency) {
        logger.debug("=== START ===");

        Wallets cashFromBank = findWalletEntry(environment.getRequiredProperty("default.bank.username"), currency.getCode());

        logger.debug("    BANK: " + cashFromBank.getAmount() + " " + cashFromBank.getCurrency() + " -> " + currency.getAmount() + " " + currency.getCode());

        if (cashFromBank.getAmount() <= 0 || cashFromBank.getAmount() < currency.getAmount()) {
            logger.debug("    USER can't buy " + currency.getAmount() + " " + currency.getCode() + " from BANK");
            logger.debug("=== END ===");
            return;
        }

        cashFromBank.setAmount(cashFromBank.getAmount() - currency.getAmount());
        walletsRepository.save(cashFromBank);

        logger.debug("    BANK: " + cashFromBank.getAmount() + " " + cashFromBank.getCurrency());

        Wallets plnFromUser = findWalletEntry(username, "PLN");
        currency.setSellPrice(exchangeRateService.getExchangeRate().getItems().stream()
                .filter(c -> c.getCode().equals(currency.getCode()))
                .findFirst().get()
                .getSellPrice());

        logger.debug("    Actual purchase price: " + currency.getUnit() + " " + currency.getCode() + " = " + currency.getSellPrice() + " PLN");
        logger.debug("    USER: " + plnFromUser.getAmount() + " " + plnFromUser.getCurrency() + " -> "
                + (currency.getAmount() * currency.getSellPrice() / currency.getUnit()) + " " + plnFromUser.getCurrency());

        if (plnFromUser.getAmount() <= 0 || plnFromUser.getAmount() < (currency.getAmount() * currency.getSellPrice() / currency.getUnit())) {
            logger.debug("    USER can't buy currencies for " + (currency.getAmount() * currency.getSellPrice()) + " PLN");
            logger.debug("=== END ===");
            return;
        }

        plnFromUser.setAmount(plnFromUser.getAmount() - (currency.getAmount() * currency.getSellPrice() / currency.getUnit()));
        walletsRepository.save(plnFromUser);

        logger.debug("    USER: " + plnFromUser.getAmount() + " " + plnFromUser.getCurrency());

        Wallets plnFromBank = findWalletEntry(environment.getRequiredProperty("default.bank.username"), "PLN");

        logger.debug("    BANK: " + plnFromBank.getAmount() + " " + plnFromBank.getCurrency() + " <- "
                + (currency.getAmount() * currency.getSellPrice() / currency.getUnit()) + " " + plnFromBank.getCurrency());

        plnFromBank.setAmount(plnFromBank.getAmount() + (currency.getAmount() * currency.getSellPrice() / currency.getUnit()));
        walletsRepository.save(plnFromBank);

        logger.debug("    BANK: " + plnFromBank.getAmount() + " " + plnFromBank.getCurrency());

        Wallets cashFromUser = findWalletEntry(username, currency.getCode());

        logger.debug("    USER: " + cashFromUser.getAmount() + " " + cashFromUser.getCurrency() + " <- " + currency.getAmount() + " " + currency.getCode());

        cashFromUser.setAmount(cashFromUser.getAmount() + currency.getAmount());
        walletsRepository.save(cashFromUser);

        logger.debug("    USER: " + cashFromUser.getAmount() + " " + cashFromUser.getCurrency());
        logger.debug("=== END ===");
    }

    @Override
    public void sellCurrency(String username, Currency currency) {
        logger.debug("=== START ===");

        Wallets cashFromUser = findWalletEntry(username, currency.getCode());

        logger.debug("    USER: " + cashFromUser.getAmount() + " " + cashFromUser.getCurrency() + " -> " + currency.getAmount() + " " + currency.getCode());

        if (cashFromUser.getAmount() <= 0 || cashFromUser.getAmount() < currency.getAmount()) {
            logger.debug("    USER can't sell " + currency.getAmount() + " " + currency.getCode() + " to BANK");
            logger.debug("=== END ===");
            return;
        }

        cashFromUser.setAmount(cashFromUser.getAmount() - currency.getAmount());
        walletsRepository.save(cashFromUser);

        logger.debug("    USER: " + cashFromUser.getAmount() + " " + cashFromUser.getCurrency());

        Wallets plnFromBank = findWalletEntry(environment.getRequiredProperty("default.bank.username"), "PLN");
        currency.setPurchasePrice(exchangeRateService.getExchangeRate().getItems().stream()
                .filter(c -> c.getCode().equals(currency.getCode()))
                .findFirst().get()
                .getPurchasePrice());

        logger.debug("    Actual sell price: " + currency.getUnit() + " " + currency.getCode() + " = " + currency.getPurchasePrice() + " PLN");
        logger.debug("    BANK: " + plnFromBank.getAmount() + " " + plnFromBank.getCurrency() + " -> "
                + (currency.getAmount() * currency.getPurchasePrice() / currency.getUnit()) + " " + plnFromBank.getCurrency());

        if (plnFromBank.getAmount() <= 0 || plnFromBank.getAmount() < (currency.getAmount() * currency.getPurchasePrice() / currency.getUnit())) {
            logger.debug("    USER can't sell currencies for " + (currency.getAmount() * currency.getPurchasePrice() / currency.getUnit()) + " PLN");
            logger.debug("=== END ===");
            return;
        }

        plnFromBank.setAmount(plnFromBank.getAmount() - (currency.getAmount() * currency.getPurchasePrice() / currency.getUnit()));
        walletsRepository.save(plnFromBank);

        logger.debug("    BANK: " + plnFromBank.getAmount() + " " + plnFromBank.getCurrency());

        Wallets plnFromUser = findWalletEntry(username, "PLN");

        logger.debug("    USER: " + plnFromUser.getAmount() + " " + plnFromUser.getCurrency() + " <- "
                + (currency.getAmount() * currency.getPurchasePrice() / currency.getUnit()) + " " + plnFromUser.getCurrency());

        plnFromUser.setAmount(plnFromUser.getAmount() + (currency.getAmount() * currency.getPurchasePrice() / currency.getUnit()));
        walletsRepository.save(plnFromUser);

        logger.debug("    USER: " + plnFromUser.getAmount() + " " + plnFromUser.getCurrency());

        Wallets cashFromBank = findWalletEntry(environment.getRequiredProperty("default.bank.username"), currency.getCode());

        logger.debug("    BANK: " + cashFromBank.getAmount() + " " + cashFromBank.getCurrency() + " <- " + currency.getAmount() + " " + currency.getCode());

        cashFromBank.setAmount(cashFromBank.getAmount() + currency.getAmount());
        walletsRepository.save(cashFromBank);

        logger.debug("    BANK: " + cashFromBank.getAmount() + " " + cashFromBank.getCurrency());
        logger.debug("=== END ===");
    }

}
