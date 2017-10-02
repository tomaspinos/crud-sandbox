package org.jaweze.hello.security;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
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
            if (!callback.login(username.getValue(), pword)) {
                Notification.show(messages.get("login.failure"));
                username.focus();
            }
        });
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        addComponent(login);
    }

    @FunctionalInterface
    public interface LoginCallback {

        boolean login(String username, String password);
    }
}