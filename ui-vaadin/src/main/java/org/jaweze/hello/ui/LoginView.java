package org.jaweze.hello.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.VerticalLayout;
import org.jaweze.hello.security.LoginForm;
import org.jaweze.hello.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginView extends VerticalLayout implements View {

    private final AuthenticationManager authenticationManager;
    private final Messages messages;
    private final Navigator navigator;

    private final Logger logger = LoggerFactory.getLogger(LoginView.class);

    public LoginView(AuthenticationManager authenticationManager, Messages messages, Navigator navigator) {
        this.authenticationManager = authenticationManager;
        this.messages = messages;
        this.navigator = navigator;

        addComponent(new LoginForm(this::login, messages));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        logger.debug("Login view");
    }

    private boolean login(String username, String password) {
        try {
            Authentication token = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // Reinitialize the session to protect against session fixation attacks. This does not work
            // with websocket communication.
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);
            // Show the main UI
            navigator.navigateTo("grid");
            return true;
        } catch (AuthenticationException ex) {
            return false;
        }
    }
}
