package com.futureprocessing.webtask.exchangeoffice.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class AdminView extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;

    public AdminView() {
        setMargin(true);
        addComponent(new Label("Admin view"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
