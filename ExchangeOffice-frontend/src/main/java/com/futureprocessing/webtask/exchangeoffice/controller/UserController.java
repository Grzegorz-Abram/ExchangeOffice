package com.futureprocessing.webtask.exchangeoffice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.futureprocessing.webtask.exchangeoffice.model.Users;
import com.futureprocessing.webtask.exchangeoffice.service.UserService;

@Controller
@ComponentScan
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, Model model) {
        if (isLoggedIn()) {
            return "index";
        } else {
            if (error != null) {
                model.addAttribute("error", true);
            }

            if (logout != null) {
                model.addAttribute("logout", true);
            }

            return "login";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        if (isLoggedIn()) {
            return "redirect:/";
        } else {
            model.addAttribute("user", new Users());

            return "register";
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") Users user) {
        if (isLoggedIn()) {
            return "redirect:/";
        } else {
            userService.registerNewUserAccount(user);

            return "redirect:/";
        }
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
