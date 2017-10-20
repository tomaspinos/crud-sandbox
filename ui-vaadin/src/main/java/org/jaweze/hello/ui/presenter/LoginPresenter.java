package org.jaweze.hello.ui.presenter;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.jaweze.hello.ui.ViewNames;
import org.jaweze.hello.ui.view.LoginView;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringComponent
@ViewScope
public class LoginPresenter implements LoginView.LoginViewListener {

    private final AuthenticationManager authenticationManager;
    private final Navigator navigator;
    private LoginView view;

    public LoginPresenter(AuthenticationManager authenticationManager, Navigator navigator) {
        this.authenticationManager = authenticationManager;
        this.navigator = navigator;
    }

    @Override
    public void onViewEntry(LoginView view) {
        this.view = view;
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
