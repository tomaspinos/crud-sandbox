package org.jaweze.hello.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import org.jaweze.hello.CustomerApiClient;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridView extends VerticalLayout implements View {

    private final CustomerApiClient customerApiClient;
    private final Messages messages;
    private final Navigator navigator;

    final Grid<Customer> grid;

    final TextField filter;

    private final Button logoutBtn;
    private final Button addNewBtn;

    private final Logger logger = LoggerFactory.getLogger(GridView.class);

    public GridView(CustomerApiClient customerApiClient, Messages messages, Navigator navigator) {
        this.customerApiClient = customerApiClient;
        this.messages = messages;
        this.navigator = navigator;

        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.logoutBtn = new Button(messages.get("main_screen.logout"));
        this.addNewBtn = new Button(messages.get("main_screen.new_customer"));

        showMain();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        logger.debug("Grid view");
    }

    private void showMain() {
        // build layout
        HorizontalLayout logoutBar = new HorizontalLayout(logoutBtn);
        logoutBar.setWidth(100, Unit.PERCENTAGE);
        logoutBar.setComponentAlignment(logoutBtn, Alignment.TOP_RIGHT);
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);

        addComponents(logoutBar, actions, grid);

        // TODO - necessary?
//        setErrorHandler(this::handleError);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "firstName", "lastName", "birthDate");

        filter.setPlaceholder(messages.get("main_screen.filter.desc"));

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            // TODO
//            editor.editCustomer(e.getValue());
            navigator.navigateTo("editor/" + e.getValue().getId());
        });

        // Instantiate and edit new Customer the new button is clicked
        // TODO
        addNewBtn.addClickListener(e -> {
//            editor.editCustomer(new Customer("", "", null, null, null))
            navigator.navigateTo("editor");
        });

        // Listen changes made by the editor, refresh data from backend
        // TODO
//        editor.setChangeHandler(() -> {
//            editor.setVisible(false);
//            listCustomers(filter.getValue());
//        });

        // TODO logout
//        logoutBtn.addClickListener(e -> logout());

        // Initialize listing
        listCustomers(null);
    }

    void listCustomers(String filterText) {
        grid.setItems(customerApiClient.getAll(filterText));
    }
}
