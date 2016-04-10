package com.futureprocessing.webtask.exchangeoffice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.futureprocessing.webtask.exchangeoffice.model.Currency;
import com.futureprocessing.webtask.exchangeoffice.model.Users;
import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.service.ExchangeRateService;
import com.futureprocessing.webtask.exchangeoffice.service.UserService;
import com.futureprocessing.webtask.exchangeoffice.service.WalletService;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView handleRequest() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout) {
        if (isLoggedIn()) {
            return new ModelAndView("index");
        } else {
            ModelAndView model = new ModelAndView();
            if (error != null) {
                model.addObject("error", true);
            }

            if (logout != null) {
                model.addObject("logout", true);
            }
            model.setViewName("login");

            return model;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registrationPage(Model model) {
        if (isLoggedIn()) {
            return "redirect:/";
        } else {
            model.addAttribute("user", new Users());

            return "register";
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("user") Users user) {
        if (isLoggedIn()) {
            return "redirect:/";
        } else {
            userService.registerNewUserAccount(user);

            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/wallet", method = RequestMethod.GET)
    public String showWallet(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<Currency> currencies = exchangeRateService.getExchangeRate().getItems();
        List<Wallets> wallet = walletService.loadWallet(auth.getName(), currencies);
        double sumValue = walletService.countSumValue(wallet);

        model.addAttribute("currencies", currencies);
        model.addAttribute("wallet", wallet);
        model.addAttribute("sumValue", sumValue);

        return "wallet";
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public String initWalletPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<Wallets> wallet = walletService.loadWallet(auth.getName());

        model.addAttribute("newWallet", new Wallets());
        model.addAttribute("wallet", wallet);

        return "init";
    }

    @RequestMapping(value = { "/init/save" }, method = RequestMethod.POST)
    public String saveNewWallet(@ModelAttribute("wallet") Wallets wallet, final RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        walletService.saveWallet(auth.getName(), wallet);

        return "redirect:/init";
    }

    @RequestMapping(value = "/init/{operation}/{username}/{currency}", method = RequestMethod.GET)
    public String editRemoveWallet(@PathVariable("operation") String operation, @PathVariable("username") String username,
            @PathVariable("currency") String currency, final RedirectAttributes redirectAttributes, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (operation.equals("delete")) {
            walletService.deleteFromWallet(username, currency);
        } else if (operation.equals("edit")) {
            model.addAttribute("newWallet", walletService.findWalletEntry(username, currency));
            model.addAttribute("wallet", walletService.loadWallet(auth.getName()));

            return "init";
        }

        return "redirect:/init";
    }

    private boolean isLoggedIn() {
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
