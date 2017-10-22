package org.jaweze.hello.security;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.jaweze.hello.utils.Messages;

public class LoginForm extends VerticalLayout {

    private final Messages messages;

    public LoginForm(LoginCallback callback, Messages messages) {
        this.messages = messages;
        setMargin(true);
        setSpacing(true);

        TextField username = new TextField(messages.get("login.username"));
        addComponent(username);

        PasswordField password = new PasswordField(messages.get("login.password"));
        addComponent(password);

        Button login = new Button(messages.get("login.login"), evt -> {
            String pword = password.getValue();
            password.setValue("");
            callback.login(username.getValue(), pword);
        });
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        addComponent(login);
    }

    @FunctionalInterface
    public interface LoginCallback {

        void login(String username, String password);
    }
}