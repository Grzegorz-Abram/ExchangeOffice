package com.futureprocessing.webtask.exchangeoffice.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.service.WalletService;

@Controller
@ComponentScan
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    ExchangeRateController exchangeRateController;

    @Autowired
    UserController userController;

    @RequestMapping(value = { "/", "/wallet" }, method = RequestMethod.GET)
    public String home(Model model) {
        if (userController.isLoggedIn()) {
            prepareIndexPage(model);
        }

        return "index";
    }

    @RequestMapping(value = "/wallet/edit", method = RequestMethod.GET)
    public String edit(Model model) {
        prepareEditPage(model);
        return "edit";
    }

    @RequestMapping(value = { "/wallet/save" }, method = RequestMethod.POST)
    public String saveWalletEntry(@Valid Wallets newWallet, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareEditPage(model);
            addErrorAttribute(model, bindingResult);
            return "edit";
        }

        walletService.saveWallet(userController.getUsername(), newWallet);

        return "redirect:/wallet/edit";
    }

    @RequestMapping(value = "/wallet/{operation}/{currency}", method = RequestMethod.GET)
    public String operationsOnWalletEntry(@PathVariable("operation") String operation, @PathVariable("currency") String currency,
            final RedirectAttributes redirectAttributes, Model model) {

        if (operation.equals("delete")) {
            walletService.deleteFromWallet(userController.getUsername(), currency);
        } else if (operation.equals("edit")) {
            prepareEditPage(model, currency);
            return "edit";
        } else if (operation.equals("sell") || operation.equals("buy")) {
            prepareIndexPage(model);

            Currency newCurrency = new Currency();
            newCurrency.setCode(currency);
            newCurrency.setUnit(exchangeRateController.getCurrencies().stream()
                    .filter(c -> c.getCode().equals(currency))
                    .findFirst().get()
                    .getUnit());

            if (operation.equals("sell")) {
                model.addAttribute("currencyToSell", newCurrency);
            } else if (operation.equals("buy")) {
                model.addAttribute("currencyToBuy", newCurrency);
            }

            return "index";
        }

        return "redirect:/wallet/edit";
    }

    @RequestMapping(value = { "/wallet/buy/*" }, method = RequestMethod.POST)
    public String buyWalletEntry(@Valid Currency currency, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareIndexPage(model);
            addErrorAttribute(model, bindingResult);
            model.addAttribute("currencyToBuy", currency);
            return "index";
        }

        currency.setAmount(currency.getAmount() * currency.getUnit());
        walletService.buyCurrency(userController.getUsername(), currency);

        return "redirect:/";
    }

    @RequestMapping(value = { "/wallet/sell/*" }, method = RequestMethod.POST)
    public String sellWalletEntry(@Valid Currency currency, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareIndexPage(model);
            addErrorAttribute(model, bindingResult);
            model.addAttribute("currencyToSell", currency);
            return "index";
        }

        walletService.sellCurrency(userController.getUsername(), currency);

        return "redirect:/";
    }

    private void prepareIndexPage(Model model) {
        List<Wallets> wallet = walletService.loadWalletWithPrices(userController.getUsername(), exchangeRateController.getCurrencies());

        model.addAttribute("currencies", exchangeRateController.getCurrencies());
        model.addAttribute("publicationDate", exchangeRateController.getCurrenciesPublicationDate());
        model.addAttribute("wallet", wallet.stream()
                .filter(w -> !w.getCurrency().equals("PLN"))
                .filter(w -> w.getAmount() > 0.0)
                .collect(Collectors.toList()));
        model.addAttribute("amountPLN", wallet.stream()
                .filter(w -> w.getCurrency().equals("PLN"))
                .findFirst().get()
                .getAmount());
    }

    private void prepareEditPage(Model model) {
        prepareEditPage(model, "");
    }

    private void prepareEditPage(Model model, String currency) {
        model.addAttribute("newWallet", walletService.findWalletEntry(userController.getUsername(), currency));
        model.addAttribute("wallet", walletService.loadWallet(userController.getUsername()).stream()
                .filter(w -> !w.getCurrency().equals("PLN"))
                .filter(w -> w.getAmount() > 0.0)
                .collect(Collectors.toList()));
        model.addAttribute("allCurrencies", exchangeRateController.getCurrencies());
    }

    void addErrorAttribute(Model model, BindingResult bindingResult) {
        model.addAttribute("error", bindingResult.getFieldError().getField() + ": " + bindingResult.getFieldError().getDefaultMessage());
    }

}
