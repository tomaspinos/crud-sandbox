package org.jaweze.hello.ui.presenter;

import com.vaadin.navigator.Navigator;
import org.jaweze.hello.CustomerApiClient;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.ui.ViewNames;
import org.jaweze.hello.ui.model.EditorModel;
import org.jaweze.hello.ui.view.EditorView;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class EditorViewPresenter implements EditorView.EditorViewListener {

    private final EditorView view;
    private final EditorModel model;
    private final CustomerApiClient customerApiClient;
    private final Navigator navigator;

    public EditorViewPresenter(EditorView view, CustomerApiClient customerApiClient, Navigator navigator) {
        this.view = view;
        this.customerApiClient = customerApiClient;
        this.navigator = navigator;

        model = new EditorModel();
        view.addListener(this);
    }

    @Override
    public void onViewEntry(String parameters) {
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
