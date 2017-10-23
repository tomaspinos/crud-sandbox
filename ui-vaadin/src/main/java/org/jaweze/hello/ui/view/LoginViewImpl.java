package org.jaweze.hello.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.jaweze.hello.ui.LoginLayoutImpl;
import org.jaweze.hello.ui.ViewNames;
import org.jaweze.hello.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringView(name = ViewNames.LOGIN)
public class LoginViewImpl extends VerticalLayout implements View, LoginView {

    private final LoginViewListener listener;
    private final Messages messages;

    private final Logger logger = LoggerFactory.getLogger(LoginViewImpl.class);

    public LoginViewImpl(LoginViewListener listener, Messages messages) {
        this.listener = listener;
        this.messages = messages;

        addComponent(new LoginLayoutImpl(listener::onLogin, messages));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        logger.debug("Login view");
        listener.onViewEntry(this);
    }

    @Override
    public void showLoginFailure() {
        Notification.show(messages.get("login.failure"));
    }
}
