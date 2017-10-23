package org.jaweze.hello.ui.view;

import org.jaweze.hello.model.Customer;

import java.util.List;

public interface GridView {

    void listCustomers(List<Customer> customers);

    interface GridViewListener {

        void onViewEntry(GridView view);

        void onFilterSpecified(String filterText);

        void onCustomerSelected(Customer customer);

        void onAddNewCustomer();
    }
}
