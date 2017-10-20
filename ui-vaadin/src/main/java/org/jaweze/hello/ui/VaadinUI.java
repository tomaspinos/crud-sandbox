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

    private final Messages messages;

    @Autowired
    public VaadinUI(AuthenticationManager authenticationManager, CustomerApiClient customerApiClient, Messages messages) {
        this.messages = messages;

        Navigator navigator = new Navigator(this, this);

        LoginView loginView = new LoginView(messages);
        new LoginPresenter(loginView, authenticationManager, navigator);
        navigator.addView("", loginView);

        GridView gridView = new GridView(messages);
        new GridPresenter(gridView, customerApiClient, navigator);
        navigator.addView(ViewNames.GRID, gridView);

        EditorView editorView = new EditorView(messages);
        new EditorViewPresenter(editorView, customerApiClient, navigator);
        navigator.addView(ViewNames.EDITOR, editorView);
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle(messages.get("main_screen.title"));
    }
}
