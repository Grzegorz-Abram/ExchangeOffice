package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.List;

import com.futureprocessing.webtask.exchangeoffice.model.Wallets;

public interface WalletService {

    List<Wallets> loadWallet(String username);

}
