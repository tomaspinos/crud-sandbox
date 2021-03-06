package org.jaweze.hello.ui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ClassResource;
import org.jaweze.hello.utils.Messages;

public class LoginLayoutImpl extends LoginLayout {

    private final Messages messages;

    public LoginLayoutImpl(LoginCallback callback, Messages messages) {
        super();
        this.topLogo.setSource(new ClassResource(LoginLayoutImpl.class,"vaadin-logo-hi.png"));
        this.messages = messages;

        loginButton.setCaption(messages.get("login.login"));
        usernameLabel.setValue(messages.get("login.username"));
        passwordLabel.setValue(messages.get("login.password"));

        loginButton.addClickListener(evt -> {
            String pword = password.getValue();
            password.setValue("");
            callback.login(username.getValue(), pword);
        });

        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    @FunctionalInterface
    public interface LoginCallback {
        void login(String username, String password);
    }

}
