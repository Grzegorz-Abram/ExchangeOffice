package com.futureprocessing.webtask.exchangeoffice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futureprocessing.webtask.exchangeoffice.controller.UserController;
import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

/**
 * This demonstrates how you can control access to views.
 */
@Component
public class SampleViewAccessControl implements ViewAccessControl {

    @Autowired
    UserController userController;

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
        if (beanName.equals("adminView")) {
            return userController.hasRole("ROLE_ADMIN") || userController.hasRole("ADMIN");
        } else {
            return userController.hasRole("ROLE_USER") || userController.hasRole("USER");
        }
    }
}
