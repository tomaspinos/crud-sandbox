package org.jaweze.hello.ui.presenter;

import com.vaadin.navigator.Navigator;
import org.jaweze.hello.CustomerApiClient;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.ui.ViewNames;
import org.jaweze.hello.ui.model.GridModel;
import org.jaweze.hello.ui.view.GridView;

public class GridPresenter implements GridView.GridViewListener {

    private final GridView view;
    private final GridModel model;
    private final CustomerApiClient customerApiClient;
    private final Navigator navigator;

    public GridPresenter(GridView view, CustomerApiClient customerApiClient, Navigator navigator) {
        this.view = view;
        this.customerApiClient = customerApiClient;
        this.navigator = navigator;

        model = new GridModel();
        view.addListener(this);
    }

    @Override
    public void onViewEntry() {
        model.setCustomers(customerApiClient.getAll(null));
        view.listCustomers(model.getCustomers());
    }

    @Override
    public void onFilterSpecified(String filterText) {
        model.setCustomers(customerApiClient.getAll(filterText));
        view.listCustomers(model.getCustomers());
    }

    @Override
    public void onCustomerSelected(Customer customer) {
        if (customer != null) {
            navigator.navigateTo(ViewNames.EDITOR + "/" + customer.getId());
        }
    }

    @Override
    public void onAddNewCustomer() {
        navigator.navigateTo(ViewNames.EDITOR);
    }
}
