package org.jaweze.hello.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.jaweze.hello.CustomerApiClient;
import org.jaweze.hello.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;

@SpringUI
public class VaadinUI extends UI {

    private final AuthenticationManager authenticationManager;

    private final Navigator navigator;

    private final CustomerEditor editor;

    private final Messages messages;

    @Autowired
    public VaadinUI(AuthenticationManager authenticationManager, CustomerApiClient customerApiClient, CustomerEditor editor, Messages messages) {
        this.authenticationManager = authenticationManager;
        this.editor = editor;
        this.messages = messages;

        navigator = new Navigator(this, this);
        navigator.addView("", new LoginView(authenticationManager, messages, navigator));
        navigator.addView("grid", new GridView(customerApiClient, messages));
        navigator.addView("editor", new EditorView());
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle(messages.get("main_screen.title"));
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
}
