package org.jaweze.hello;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.model.CustomerRepository;
import org.jaweze.hello.security.LoginForm;
import org.jaweze.hello.security.SecurityUtils;
import org.jaweze.hello.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

@SpringUI
public class VaadinUI extends UI {

    private final AuthenticationManager authenticationManager;

    private final CustomerRepository repo;

    private final CustomerEditor editor;

    private final Messages messages;

    final Grid<Customer> grid;

    final TextField filter;

    private final Button logoutBtn;
    private final Button addNewBtn;

    @Autowired
    public VaadinUI(AuthenticationManager authenticationManager, CustomerRepository repo, CustomerEditor editor, Messages messages) {
        this.authenticationManager = authenticationManager;
        this.repo = repo;
        this.editor = editor;
        this.messages = messages;
        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.logoutBtn = new Button(messages.get("main_screen.logout"));
        this.addNewBtn = new Button(messages.get("main_screen.new_customer"));
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle(messages.get("main_screen.title"));
        if (SecurityUtils.isLoggedIn()) {
            showMain();
        } else {
            showLogin();
        }
    }

    private void showLogin() {
        setContent(new LoginForm(this::login, messages));
    }

    private void showMain() {
        // build layout
        HorizontalLayout logoutBar = new HorizontalLayout(logoutBtn);
        logoutBar.setWidth(100, Unit.PERCENTAGE);
        logoutBar.setComponentAlignment(logoutBtn, Alignment.TOP_RIGHT);
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(logoutBar, actions, grid, editor);
        setContent(mainLayout);

        // TODO - necessary?
        setErrorHandler(this::handleError);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("id", "firstName", "lastName", "birthDate");

        filter.setPlaceholder(messages.get("main_screen.filter.desc"));

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "", null, null, null)));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });

        // TODO logout
        logoutBtn.addClickListener(e -> logout());

        // Initialize listing
        listCustomers(null);
    }

    private boolean login(String username, String password) {
        try {
            Authentication token = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // Reinitialize the session to protect against session fixation attacks. This does not work
            // with websocket communication.
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);
            // Now when the session is reinitialized, we can enable websocket communication. Or we could have just
            // used WEBSOCKET_XHR and skipped this step completely.
            getPushConfiguration().setTransport(Transport.WEBSOCKET);
            getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
            // Show the main UI
            showMain();
            return true;
        } catch (AuthenticationException ex) {
            return false;
        }
    }

    private void logout() {
        getPage().reload();
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

    void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        } else {
            grid.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
        }
    }
}
