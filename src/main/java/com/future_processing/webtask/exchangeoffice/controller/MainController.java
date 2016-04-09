package com.future_processing.webtask.exchangeoffice.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.future_processing.webtask.exchangeoffice.model.Users;
import com.future_processing.webtask.exchangeoffice.service.UserService;

@Controller
public class MainController {

	@Autowired
	private UserService userService;

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
