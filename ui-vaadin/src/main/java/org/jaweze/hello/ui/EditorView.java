package org.jaweze.hello.ui;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.jaweze.hello.CustomerApiClient;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.model.MarriageStatus;
import org.jaweze.hello.model.Sex;
import org.jaweze.hello.security.CustomRoles;
import org.jaweze.hello.security.SecurityUtils;
import org.jaweze.hello.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public class EditorView extends VerticalLayout implements View {

    private final CustomerApiClient customerApiClient;
    private final Messages messages;
    private final Navigator navigator;

    /**
     * The currently edited customer
     */
    private Customer customer;

    /* Fields to edit properties in Customer entity */
    TextField firstName;
    TextField lastName;
    DateField birthDate;
    ComboBox<Sex> sex;
    ComboBox<MarriageStatus> marriageStatus;

    /* Action buttons */
    Button save;
    Button cancel;
    Button delete;
    CssLayout actions;

    Binder<Customer> binder = new Binder<>(Customer.class);

    private final Logger logger = LoggerFactory.getLogger(EditorView.class);

    public EditorView(CustomerApiClient customerApiClient, Messages messages, Navigator navigator) {
        this.customerApiClient = customerApiClient;
        this.messages = messages;
        this.navigator = navigator;

        firstName = new TextField(messages.get("customer_editor.firstName"));
        lastName = new TextField(messages.get("customer_editor.lastName"));
        birthDate = new DateField(messages.get("customer_editor.birthDate"));
        sex = new ComboBox<>(messages.get("customer_editor.sex"), Arrays.asList(Sex.values()));
        marriageStatus = new ComboBox<>(messages.get("customer_editor.marriageStatus"), Arrays.asList(MarriageStatus.values()));

        save = new Button(messages.get("customer_editor.save"), FontAwesome.SAVE);
        cancel = new Button(messages.get("customer_editor.cancel"));
        delete = new Button(messages.get("customer_editor.delete"), FontAwesome.TRASH_O);
        actions = new CssLayout(save, cancel, delete);

        sex.setItemCaptionGenerator(item -> messages.get("codebook.sex." + item.name()));

        marriageStatus.setItemCaptionGenerator(item -> messages.get("codebook.marriageStatus." + item.name()));

        addComponents(firstName, lastName, birthDate, sex, marriageStatus, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addClickListener(e -> customerApiClient.update(customer));

        cancel.addClickListener(e -> editCustomer(customer));

        delete.addClickListener(e -> customerApiClient.delete(customer.getId()));

        setVisible(false);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent e) {
        logger.debug("Editor view for parameters {}", e.getParameters());

        if (StringUtils.hasText(e.getParameters())) {
            Optional<Customer> maybeCustomer = customerApiClient.getById(Long.parseLong(e.getParameters()));
            editCustomer(maybeCustomer.orElse(null));
        } else {
            editCustomer(null);
        }
    }

    private final void editCustomer(Customer c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            customer = customerApiClient.getById(c.getId()).get();
        } else {
            customer = c;
        }
        cancel.setVisible(persisted);

        checkAuthorization();

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(customer);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
        // Select all text in firstName field automatically
        firstName.selectAll();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }

    private void checkAuthorization() {
        boolean isManager = SecurityUtils.hasRole(CustomRoles.MANAGER);

        firstName.setEnabled(isManager);
        lastName.setEnabled(isManager);
        save.setEnabled(isManager);
        cancel.setEnabled(isManager);
        delete.setEnabled(isManager);
    }

    public interface ChangeHandler {

        void onChange();
    }
}
