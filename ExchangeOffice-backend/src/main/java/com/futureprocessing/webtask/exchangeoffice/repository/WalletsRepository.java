package com.futureprocessing.webtask.exchangeoffice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.model.WalletsId;

public interface WalletsRepository extends CrudRepository<Wallets, WalletsId> {

    List<Wallets> findByUsernameOrderByCurrency(String username);

}
