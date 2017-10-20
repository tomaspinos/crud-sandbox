package org.jaweze.hello.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

public class GridView extends VerticalLayout implements View {

    private final Messages messages;

    private final List<GridViewListener> listeners = new ArrayList<>();

    private final Grid<Customer> grid;
    private final TextField filter;
    private final Button logoutBtn;
    private final Button addNewBtn;

    private final Logger logger = LoggerFactory.getLogger(GridView.class);

    public interface GridViewListener {

        void onViewEntry();

        void onFilterSpecified(String filterText);

        void onCustomerSelected(Customer customer);

        void onAddNewCustomer();
    }

    public GridView(Messages messages) {
        this.messages = messages;

        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.logoutBtn = new Button(messages.get("main_screen.logout"));
        this.addNewBtn = new Button(messages.get("main_screen.new_customer"));

        showMain();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        logger.debug("Grid view");
        listeners.forEach(GridViewListener::onViewEntry);
    }

    public void addListener(GridViewListener listener) {
        listeners.add(listener);
    }

    public void listCustomers(List<Customer> customers) {
        grid.setItems(customers);
    }

    private void showMain() {
        // build layout
        HorizontalLayout logoutBar = new HorizontalLayout(logoutBtn);
        logoutBar.setWidth(100, Unit.PERCENTAGE);
        logoutBar.setComponentAlignment(logoutBtn, Alignment.TOP_RIGHT);
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);

        addComponents(logoutBar, actions, grid);

        // TODO - necessary?
        setErrorHandler(this::handleError);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "firstName", "lastName", "birthDate");

        filter.setPlaceholder(messages.get("main_screen.filter.desc"));

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listeners.forEach(l -> l.onFilterSpecified(e.getValue())));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> listeners.forEach(l -> l.onCustomerSelected(e.getValue())));

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> listeners.forEach(GridViewListener::onAddNewCustomer));

        logoutBtn.addClickListener(e -> logout());

        // Initialize listing
        listCustomers(null);
    }

    private void logout() {
        getUI().getPage().reload();
        getSession().close();
    }

    private void handleError(com.vaadin.server.ErrorEvent event) {
        Throwable t = DefaultErrorHandler.findRelevantThrowable(event.getThrowable());
        if (t instanceof AccessDeniedException) {
            Notification.show(messages.get("main_screen.no_permission"), Notification.Type.WARNING_MESSAGE);
        } else {
            DefaultErrorHandler.doDefault(event);
        }
    }
}
