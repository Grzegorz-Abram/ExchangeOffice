package com.future_processing.webtask.exchangeoffice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.future_processing.webtask.exchangeoffice.dao.WalletsDao;
import com.future_processing.webtask.exchangeoffice.model.Wallets;

@Service("walletService")
public class WalletServiceImpl implements WalletService {

	WalletsDao walletsDao;

	@Autowired
	public void setWalletsDao(WalletsDao walletsDao) {
		this.walletsDao = walletsDao;
	}

	@Override
	public List<Wallets> loadWallet(String username) {
		return walletsDao.loadWallet(username);
	}

}
