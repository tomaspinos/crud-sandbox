package org.jaweze.hello.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginPresenter implements LoginView.LoginViewListener {

    private final LoginView view;
    private final AuthenticationManager authenticationManager;
    private final Navigator navigator;

    public LoginPresenter(LoginView view, AuthenticationManager authenticationManager, Navigator navigator) {
        this.view = view;
        this.authenticationManager = authenticationManager;
        this.navigator = navigator;

        view.addListener(this);
    }

    @Override
    public void onViewEntry() {
        // do nothing
    }

    @Override
    public void onLogin(String username, String password) {
        try {
            Authentication token = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // Reinitialize the session to protect against session fixation attacks. This does not work
            // with websocket communication.
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);
            // Show the main UI
            navigator.navigateTo(ViewNames.GRID);
        } catch (AuthenticationException ex) {
            view.showLoginFailure();
        }
    }
}
