package org.jaweze.hello.ui.presenter;

import com.vaadin.navigator.Navigator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.jaweze.hello.CustomerApiClient;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.ui.ViewNames;
import org.jaweze.hello.ui.model.EditorModel;
import org.jaweze.hello.ui.view.EditorView;
import org.springframework.util.StringUtils;

import java.util.Optional;

@SpringComponent
@ViewScope
public class EditorPresenter implements EditorView.EditorViewListener {

    private final EditorModel model;
    private final CustomerApiClient customerApiClient;
    private final Navigator navigator;
    private EditorView view;

    public EditorPresenter(EditorModel model, CustomerApiClient customerApiClient, Navigator navigator) {
        this.model = model;
        this.customerApiClient = customerApiClient;
        this.navigator = navigator;
    }

    @Override
    public void onViewEntry(EditorView view, String parameters) {
        this.view = view;

        if (StringUtils.hasText(parameters)) {
            long customerId = Long.parseLong(parameters);
            Optional<Customer> maybeCustomer = customerApiClient.getById(customerId);
            if (maybeCustomer.isPresent()) {
                model.setCustomer(maybeCustomer.get());
                view.showCustomer(model.getCustomer());
            } else {
                view.showNotFoundError();
            }
        } else {
            model.setCustomer(new Customer());
            view.showCustomer(model.getCustomer());
        }
    }

    @Override
    public void onBack() {
        navigator.navigateTo(ViewNames.GRID);
    }

    @Override
    public void onSave() {
        customerApiClient.update(model.getCustomer());
        onBack();
    }

    @Override
    public void onDelete() {
        customerApiClient.delete(model.getCustomer().getId());
        onBack();
    }
}
