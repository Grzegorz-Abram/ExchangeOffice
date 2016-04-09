package com.future_processing.webtask.exchangeoffice.service;

import java.util.List;

import com.future_processing.webtask.exchangeoffice.model.Wallets;

public interface WalletService {

	List<Wallets> loadWallet(String username);

}
