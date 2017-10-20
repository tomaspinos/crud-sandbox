package org.jaweze.hello.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.jaweze.hello.security.LoginForm;
import org.jaweze.hello.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LoginView extends VerticalLayout implements View {

    private final Messages messages;

    private final List<LoginViewListener> listeners = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(LoginView.class);

    public interface LoginViewListener {

        void onViewEntry();

        void onLogin(String username, String password);
    }

    public LoginView(Messages messages) {
        this.messages = messages;

        addComponent(new LoginForm((username, password) -> listeners.forEach(l -> l.onLogin(username, password)), messages));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        logger.debug("Login view");
        listeners.forEach(LoginViewListener::onViewEntry);
    }

    public void addListener(LoginViewListener listener) {
        listeners.add(listener);
    }

    public void showLoginFailure() {
        Notification.show(messages.get("login.failure"));
    }
}
