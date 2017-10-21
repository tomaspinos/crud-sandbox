package org.jaweze.hello.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.ui.ViewNames;
import org.jaweze.hello.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@SpringView(name = ViewNames.GRID)
public class GridView extends VerticalLayout implements View {

    private final GridViewListener listener;
    private final Messages messages;

    private final Grid<Customer> grid;
    private final TextField filter;
    private final Button logoutBtn;
    private final Button addNewBtn;

    private final Logger logger = LoggerFactory.getLogger(GridView.class);

    public GridView(GridViewListener listener, Messages messages) {
        this.listener = listener;
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
        listener.onViewEntry(this);
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
        filter.addValueChangeListener(e -> listener.onFilterSpecified(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> listener.onCustomerSelected(e.getValue()));

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> listener.onAddNewCustomer());

        logoutBtn.addClickListener(e -> logout());
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
