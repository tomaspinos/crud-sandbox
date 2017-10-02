package org.jaweze.hello;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.model.MarriageStatus;
import org.jaweze.hello.model.Sex;
import org.jaweze.hello.security.CustomRoles;
import org.jaweze.hello.security.SecurityUtils;
import org.jaweze.hello.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in VaadinUI.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX. See e.g. AbstractForm in Viritin
 * (https://vaadin.com/addon/viritin).
 */
@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout {

    private final CustomerRepository repository;

    private final Messages messages;

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

    @Autowired
    public CustomerEditor(CustomerRepository repository, Messages messages) {
        this.repository = repository;
        this.messages = messages;

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
        save.addClickListener(e -> repository.save(customer));

        cancel.addClickListener(e -> editCustomer(customer));

        delete.addClickListener(e -> repository.delete(customer));

        setVisible(false);
    }

    public interface ChangeHandler {

        void onChange();
    }

    public final void editCustomer(Customer c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            customer = repository.findOne(c.getId());
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
}
