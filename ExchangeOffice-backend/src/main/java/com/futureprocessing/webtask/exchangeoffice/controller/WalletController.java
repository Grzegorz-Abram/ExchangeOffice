package com.futureprocessing.webtask.exchangeoffice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.service.WalletService;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @RequestMapping(value = "/wallet/{username}", method = RequestMethod.GET)
    public ResponseEntity<List<Wallets>> listAllWalletEntries(@PathVariable("username") String username) {
        List<Wallets> walletEntries = walletService.findByUsername(username);
        if (walletEntries.isEmpty()) {
            return new ResponseEntity<List<Wallets>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Wallets>>(walletEntries, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallet/{username}/{currency}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wallets> getWalletEntry(@PathVariable("username") String username, @PathVariable("currency") String currency) {
        Wallets walletEntry = walletService.findByUsernameAndCurrency(username, currency);
        if (walletEntry == null) {
            return new ResponseEntity<Wallets>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Wallets>(walletEntry, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallet/{username}", method = RequestMethod.POST)
    public ResponseEntity<Void> createWalletEntry(@PathVariable("username") String username, @RequestBody Wallets walletEntry, UriComponentsBuilder ucBuilder) {
        Wallets currentWalletEntry = walletService.findByUsernameAndCurrency(username, walletEntry.getCurrency());
        if (currentWalletEntry != null) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        if (!username.equals(walletEntry.getUsername())) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        walletService.saveWallet(walletEntry);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/wallet/{username}").buildAndExpand(walletEntry.getCurrency()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/wallet/{username}/{currency}", method = RequestMethod.PUT)
    public ResponseEntity<Wallets> updateWalletEntry(@PathVariable("username") String username, @PathVariable("currency") String currency,
            @RequestBody Wallets walletEntry) {
        Wallets currentWalletEntry = walletService.findByUsernameAndCurrency(username, currency);
        if (currentWalletEntry == null) {
            return new ResponseEntity<Wallets>(HttpStatus.NOT_FOUND);
        }
        if (!username.equals(walletEntry.getUsername())) {
            return new ResponseEntity<Wallets>(HttpStatus.CONFLICT);
        }
        if (!currency.equals(walletEntry.getCurrency())) {
            return new ResponseEntity<Wallets>(HttpStatus.CONFLICT);
        }

        walletService.saveWallet(walletEntry);

        return new ResponseEntity<Wallets>(walletEntry, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallet/{username}/{currency}", method = RequestMethod.DELETE)
    public ResponseEntity<Wallets> deleteWalletEntry(@PathVariable("username") String username, @PathVariable("currency") String currency) {
        Wallets currentWalletEntry = walletService.findByUsernameAndCurrency(username, currency);
        if (currentWalletEntry == null) {
            return new ResponseEntity<Wallets>(HttpStatus.NOT_FOUND);
        }

        walletService.deleteWalletByUsernameAndCurrency(username, currency);

        return new ResponseEntity<Wallets>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/wallet/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<Wallets> deleteAllWalletEntries(@PathVariable("username") String username) {
        walletService.deleteAllWalletEntries(username);

        return new ResponseEntity<Wallets>(HttpStatus.NO_CONTENT);
    }

    // @RequestMapping(value = { "/wallet/save/{username}" }, method = RequestMethod.POST)
    // public void saveWalletEntry(@PathVariable("username") String username, @Valid Wallets newWallet) {
    // walletService.saveWallet(username, newWallet);
    // }
    //
    // @RequestMapping(value = "/wallet/delete/{username}/{currency}", method = RequestMethod.DELETE)
    // public void operationsOnWalletEntry(@PathVariable("username") String username, @PathVariable("currency") String currency) {
    // walletService.deleteFromWallet(username, currency);
    // }
    //
    // @RequestMapping(value = { "/wallet/buy/{username}" }, method = RequestMethod.POST)
    // public void buyWalletEntry(@PathVariable("username") String username, @Valid Currency currency) {
    // try {
    // currency.setAmount(currency.getAmount() * currency.getUnit());
    // walletService.buyCurrency(username, currency);
    // } catch (Exception e) {
    // }
    // }
    //
    // @RequestMapping(value = { "/wallet/sell/{username}" }, method = RequestMethod.POST)
    // public void sellWalletEntry(@PathVariable("username") String username, @Valid Currency currency) {
    // try {
    // walletService.sellCurrency(username, currency);
    // } catch (Exception e) {
    // }
    // }

}
