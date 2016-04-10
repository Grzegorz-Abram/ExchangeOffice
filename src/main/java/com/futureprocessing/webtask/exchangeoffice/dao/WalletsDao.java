package com.futureprocessing.webtask.exchangeoffice.dao;

import java.util.List;

import com.futureprocessing.webtask.exchangeoffice.model.Wallets;

public interface WalletsDao {

    List<Wallets> loadWallet(String username);

    void addToWallet(Wallets entry);

    void deleteFromWallet(String username, String currency);

    Wallets findWalletEntry(String username, String currency);

}
