package org.jaweze.hello.ui.view;

public interface LoginView {

    void showLoginFailure();

    interface LoginViewListener {

        void onViewEntry(LoginView view);

        void onLogin(String username, String password);
    }
}
