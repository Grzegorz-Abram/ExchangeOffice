package com.futureprocessing.webtask.exchangeoffice.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = "wallet")
public class WalletView extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;

    public WalletView() {
        setMargin(true);
        addComponent(new Label("Wallet view"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
