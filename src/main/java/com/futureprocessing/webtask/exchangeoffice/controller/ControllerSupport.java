package com.futureprocessing.webtask.exchangeoffice.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.futureprocessing.webtask.exchangeoffice.model.Currencies;
import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.service.ExchangeRateService;
import com.futureprocessing.webtask.exchangeoffice.service.WalletService;

public class ControllerSupport {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private WalletService walletService;

    void prepareIndexPage(Model model) {
        model.addAttribute("currencies", getCurrencies());
        model.addAttribute("publicationDate", getCurrenciesPublicationDate());
        model.addAttribute("wallet", getWallet());
        model.addAttribute("sumValue", countSumValue());
    }

    void prepareEditPage(Model model) {
        model.addAttribute("newWallet", new Wallets());
        model.addAttribute("wallet", walletService.loadWallet(getUsername()));
        model.addAttribute("allCurrencies", getCurrencies());
    }

    void addErrorAttribute(Model model, BindingResult bindingResult) {
        model.addAttribute("error", bindingResult.getFieldError().getField() + ": " + bindingResult.getFieldError().getDefaultMessage());
    }

    void prepareEditPage(Model model, String currency) {
        model.addAttribute("newWallet", walletService.findWalletEntry(getUsername(), currency));
        model.addAttribute("wallet", walletService.loadWallet(getUsername()));
        model.addAttribute("allCurrencies", getCurrencies());
    }

    List<Wallets> getWallet() {
        return walletService.loadWallet(getUsername(), getCurrencies());
    }

    List<Currency> getCurrencies() {
        return getActualExchangeRates().getItems();
    }

    Date getCurrenciesPublicationDate() {
        return getActualExchangeRates().getPublicationDate();
    }

    Currencies getActualExchangeRates() {
        return exchangeRateService.getExchangeRate();
    }

    Double countSumValue() {
        Double valueSum;
        try {
            valueSum = getWallet()
                    .stream()
                    .mapToDouble(w -> w.getValue())
                    .sum();
        } catch (Exception e) {
            valueSum = null;
        }
        return valueSum;
    }

    String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return false;
        } else {
            for (GrantedAuthority a : auth.getAuthorities()) {
                if (a.getAuthority().equals("ROLE_ANONYMOUS")) {
                    return false;
                }
            }

            return true;
        }
    }

}
