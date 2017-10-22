package org.jaweze.hello.ui.view;

import org.jaweze.hello.model.Customer;

public interface GridViewListener {

    void onViewEntry(GridView view);

    void onFilterSpecified(String filterText);

    void onCustomerSelected(Customer customer);

    void onAddNewCustomer();
}
