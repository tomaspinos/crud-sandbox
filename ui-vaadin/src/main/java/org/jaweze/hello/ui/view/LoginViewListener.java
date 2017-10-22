package org.jaweze.hello.ui.view;

public interface LoginViewListener {

    void onViewEntry(LoginView view);

    void onLogin(String username, String password);
}
