package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.List;

import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;

public interface WalletService {

    List<Wallets> loadWallet(String username);

    List<Wallets> loadWallet(String username, List<Currency> currencies);

    double countSumValue(List<Wallets> wallet);

    List<Wallets> initWallet(String username, List<Currency> currencies);

    void saveWallet(String username, Wallets wallet);

    void deleteFromWallet(String username, String currency);

    Wallets findWalletEntry(String username, String currency);

}
