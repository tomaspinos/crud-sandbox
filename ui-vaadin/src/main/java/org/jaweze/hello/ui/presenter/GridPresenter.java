package org.jaweze.hello.ui.presenter;

import com.vaadin.navigator.Navigator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.jaweze.hello.CustomerApiClient;
import org.jaweze.hello.model.Customer;
import org.jaweze.hello.ui.ViewNames;
import org.jaweze.hello.ui.model.GridModel;
import org.jaweze.hello.ui.view.GridView;
import org.jaweze.hello.ui.view.GridViewListener;

@SpringComponent
@ViewScope
public class GridPresenter implements GridViewListener {

    private final GridModel model;
    private final CustomerApiClient customerApiClient;
    private final Navigator navigator;
    private GridView view;

    public GridPresenter(GridModel model, CustomerApiClient customerApiClient, Navigator navigator) {
        this.model = model;
        this.customerApiClient = customerApiClient;
        this.navigator = navigator;
    }

    @Override
    public void onViewEntry(GridView view) {
        this.view = view;

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
