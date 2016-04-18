package com.futureprocessing.webtask.exchangeoffice.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.futureprocessing.webtask.exchangeoffice.model.Wallets;
import com.futureprocessing.webtask.exchangeoffice.model.WalletsId;
import com.futureprocessing.webtask.exchangeoffice.repository.WalletsRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class CurrencyEditor extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final WalletsRepository repository;

    /**
     * The currently edited currency
     */
    private Wallets walletEntry;

    /* Fields to edit properties in Wallets entity */
    TextField username = new TextField("Username");
    TextField currency = new TextField("Currency");
    TextField amount = new TextField("Amount");

    /* Action buttons */
    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public CurrencyEditor(WalletsRepository repository) {
        this.repository = repository;

        addComponents(username, currency, amount, actions);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> repository.save(walletEntry));
        delete.addClickListener(e -> repository.delete(walletEntry));
        cancel.addClickListener(e -> editCurrency(walletEntry));
        setVisible(false);
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editCurrency(Wallets w) {
        final boolean persisted = ((w.getUsername() != null) && (w.getCurrency() != null));
        if (persisted) {
            // Find fresh entity for editing
            walletEntry = repository.findOne(new WalletsId(w.getUsername(), w.getCurrency()));
            if (walletEntry == null) { // TODO changeIt
                walletEntry = w;
            }
        } else {
            walletEntry = w;
        }
        cancel.setVisible(persisted);

        // Bind currency properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(walletEntry, this);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
        // Select all text in firstName field automatically
        currency.selectAll();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }

}
