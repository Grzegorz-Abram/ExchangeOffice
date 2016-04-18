package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.List;

import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;

public interface WalletService {

    List<Wallets> loadWalletEntriesByCurrency(String username, String currency);

    List<Wallets> loadWallet(String username);

    List<Wallets> loadWalletWithPrices(String username, List<Currency> currencies);

    void saveWallet(String username, Wallets wallet);

    void deleteFromWallet(String username, String currency);

    Wallets findWalletEntry(String username, String currency);

    void buyCurrency(String name, Currency currency) throws Exception;

    void sellCurrency(String name, Currency currency) throws Exception;

}
