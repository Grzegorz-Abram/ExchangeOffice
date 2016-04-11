package com.futureprocessing.webtask.exchangeoffice.controller;

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
public class WalletController extends ControllerSupport {

    @Autowired
    private WalletService walletService;

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

        walletService.saveWallet(getUsername(), newWallet);

        return "redirect:/wallet/edit";
    }

    @RequestMapping(value = "/wallet/{operation}/{currency}", method = RequestMethod.GET)
    public String operationsOnWalletEntry(@PathVariable("operation") String operation, @PathVariable("currency") String currency,
            final RedirectAttributes redirectAttributes, Model model) {

        if (operation.equals("delete")) {
            walletService.deleteFromWallet(getUsername(), currency);
        } else if (operation.equals("edit")) {
            prepareEditPage(model, currency);
            return "edit";
        } else if (operation.equals("sell")) {
            prepareIndexPage(model);
            model.addAttribute("sellCurrency", currency);

            Currency newCurrency = new Currency();
            newCurrency.setCode(currency);
            model.addAttribute("currency", newCurrency);

            return "index";
        } else if (operation.equals("buy")) {
            prepareIndexPage(model);
            model.addAttribute("buyCurrency", currency);

            Currency newCurrency = new Currency();
            newCurrency.setCode(currency);
            newCurrency.setUnit(getCurrencies()
                    .stream()
                    .filter(c -> c.getCode().equals(currency))
                    .findFirst()
                    .get()
                    .getUnit());
            model.addAttribute("currency", newCurrency);

            return "index";
        }

        return "redirect:/wallet/edit";
    }

    @RequestMapping(value = { "/wallet/buy/*" }, method = RequestMethod.POST)
    public String buyWalletEntry(@Valid Currency currency, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareIndexPage(model);
            addErrorAttribute(model, bindingResult);

            Currency newCurrency = new Currency();
            newCurrency.setCode(currency.getCode());
            newCurrency.setUnit(currency.getUnit());
            model.addAttribute("currency", newCurrency);
            model.addAttribute("buyCurrency", currency.getCode());

            return "index";
        }

        walletService.buyCurrency(getUsername(), currency);

        return "redirect:/";
    }

    @RequestMapping(value = { "/wallet/sell/*" }, method = RequestMethod.POST)
    public String sellWalletEntry(@Valid Currency currency, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            prepareIndexPage(model);
            addErrorAttribute(model, bindingResult);

            Currency newCurrency = new Currency();
            newCurrency.setCode(currency.getCode());
            model.addAttribute("currency", newCurrency);
            model.addAttribute("sellCurrency", currency.getCode());

            return "index";
        }

        walletService.sellCurrency(getUsername(), currency);

        return "redirect:/";
    }

}
