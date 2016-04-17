package com.futureprocessing.webtask.exchangeoffice.ui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class RegisterForm extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public RegisterForm() {
        setMargin(true);
        setSpacing(true);

        TextField username = new TextField("Username");
        addComponent(username);

        PasswordField password = new PasswordField("Password");
        addComponent(password);

        Button register = new Button("Register", evt -> {
            Notification.show("Registered new user...");
        });
        register.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        addComponent(register);
    }

}
