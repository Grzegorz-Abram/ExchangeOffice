package com.future_processing.webtask.exchangeoffice.dao;

import java.util.List;

import com.future_processing.webtask.exchangeoffice.model.Wallets;

public interface WalletsDao {

	List<Wallets> loadWallet(String username);

}
