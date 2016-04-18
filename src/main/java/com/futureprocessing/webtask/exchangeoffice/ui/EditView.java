package com.futureprocessing.webtask.exchangeoffice.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.service.WalletService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = "edit")
@PropertySource(value = "classpath:application.default.properties")
public class EditView extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;

    private Grid grid;
    private TextField filter;
    private Link link;
    private Button addNewBtn;

    private String username;

    @Autowired
    private CurrencyEditor editor;

    @Autowired
    private WalletService walletService;

    @Autowired
    private Environment environment;

    public EditView() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setMargin(true);

        this.grid = new Grid();
        this.filter = new TextField();
        this.addNewBtn = new Button("New currency", FontAwesome.PLUS);
        this.link = new Link("Go to Exchange", new ExternalResource("http://localhost:8080/ExchangeOffice/wallet"));
        this.username = environment.getRequiredProperty("default.user.username");

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor, link);

        // Configure layouts and components
        actions.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("username", "currency", "amount");

        filter.setInputPrompt("Filter by currency");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.addTextChangeListener(e -> listCurrencies(e.getText()));

        // Connect selected Customer to editor or hide if none is selected
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                editor.setVisible(false);
            } else {
                editor.editCurrency((Wallets) grid.getSelectedRow());
            }
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCurrency(new Wallets(username, "", 0.0)));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCurrencies(filter.getValue());
        });

        // Initialize listing
        listCurrencies(null);

        addComponent(mainLayout);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void listCurrencies(String currency) {
        if (StringUtils.isEmpty(currency)) {
            grid.setContainerDataSource(new BeanItemContainer(Wallets.class, walletService.loadWallet(username)));
        } else {
            grid.setContainerDataSource(new BeanItemContainer(Wallets.class, walletService.loadWalletEntriesByCurrency(username, currency)));
        }
    }

}
