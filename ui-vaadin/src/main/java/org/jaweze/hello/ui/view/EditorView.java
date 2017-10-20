package org.jaweze.hello.ui.view;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.model.MarriageStatus;
import org.jaweze.hello.model.Sex;
import org.jaweze.hello.security.CustomRoles;
import org.jaweze.hello.security.SecurityUtils;
import org.jaweze.hello.ui.ViewNames;
import org.jaweze.hello.ui.presenter.EditorPresenter;
import org.jaweze.hello.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringView(name = ViewNames.EDITOR)
public class EditorView extends VerticalLayout implements View {

    private final Messages messages;

    private final List<EditorViewListener> listeners = new ArrayList<>();

    /* Fields to edit properties in Customer entity */
    TextField firstName;
    TextField lastName;
    DateField birthDate;
    ComboBox<Sex> sex;
    ComboBox<MarriageStatus> marriageStatus;

    /* Action buttons */
    Button back;
    Button save;
    Button delete;
    CssLayout actions;

    Binder<Customer> binder = new Binder<>(Customer.class);

    private final Logger logger = LoggerFactory.getLogger(EditorView.class);

    public interface EditorViewListener {

        void onViewEntry(EditorView view, String parameters);

        void onBack();

        void onSave();

        void onDelete();
    }

    public EditorView(EditorPresenter presenter, Messages messages) {
        this.messages = messages;

        listeners.add(presenter);

        firstName = new TextField(messages.get("customer_editor.firstName"));
        lastName = new TextField(messages.get("customer_editor.lastName"));
        birthDate = new DateField(messages.get("customer_editor.birthDate"));
        sex = new ComboBox<>(messages.get("customer_editor.sex"), Arrays.asList(Sex.values()));
        marriageStatus = new ComboBox<>(messages.get("customer_editor.marriageStatus"), Arrays.asList(MarriageStatus.values()));

        back = new Button(messages.get("customer_editor.back"));
        save = new Button(messages.get("customer_editor.save"), FontAwesome.SAVE);
        delete = new Button(messages.get("customer_editor.delete"), FontAwesome.TRASH_O);
        actions = new CssLayout(save, delete);

        sex.setItemCaptionGenerator(item -> messages.get("codebook.sex." + item.name()));

        marriageStatus.setItemCaptionGenerator(item -> messages.get("codebook.marriageStatus." + item.name()));

        addComponents(back, firstName, lastName, birthDate, sex, marriageStatus, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        back.addClickListener(e -> listeners.forEach(EditorViewListener::onBack));

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addClickListener(e -> listeners.forEach(EditorViewListener::onSave));

        delete.addClickListener(e -> listeners.forEach(EditorViewListener::onDelete));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent e) {
        logger.debug("Editor view for parameters {}", e.getParameters());
        listeners.forEach(l -> l.onViewEntry(this, e.getParameters()));
    }

    public void addListener(EditorViewListener listener) {
        listeners.add(listener);
    }

    public final void showCustomer(Customer customer) {
        checkAuthorization();

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(customer);

        // A hack to ensure the whole form is visible
        save.focus();
        // Select all text in firstName field automatically
        firstName.selectAll();
    }

    public void showNotFoundError() {
        Notification.show(messages.get("customer_editor.notFound"), Notification.Type.WARNING_MESSAGE);
    }

    private void checkAuthorization() {
        boolean isManager = SecurityUtils.hasRole(CustomRoles.MANAGER);

        firstName.setEnabled(isManager);
        lastName.setEnabled(isManager);
        save.setEnabled(isManager);
        delete.setEnabled(isManager);
    }
}
