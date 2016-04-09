package com.futureprocessing.webtask.exchangeoffice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futureprocessing.webtask.exchangeoffice.dao.WalletsDao;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;

@Service("walletService")
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletsDao walletsDao;

    @Override
    public List<Wallets> loadWallet(String username) {
        return walletsDao.loadWallet(username);
    }

}
