package org.jaweze.hello.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.jaweze.hello.CustomerApiClient;
import org.jaweze.hello.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

@SpringUI
public class VaadinUI extends UI {

    private final AuthenticationManager authenticationManager;
    private final Navigator navigator;
    private final Messages messages;

    @Autowired
    public VaadinUI(AuthenticationManager authenticationManager, CustomerApiClient customerApiClient, Messages messages) {
        this.authenticationManager = authenticationManager;
        this.messages = messages;

        navigator = new Navigator(this, this);
        navigator.addView("", new LoginView(authenticationManager, messages, navigator));
        navigator.addView(ViewNames.GRID, new GridView(customerApiClient, messages, navigator));
        navigator.addView(ViewNames.EDITOR, new EditorView(customerApiClient, messages, navigator));
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle(messages.get("main_screen.title"));
    }
}
