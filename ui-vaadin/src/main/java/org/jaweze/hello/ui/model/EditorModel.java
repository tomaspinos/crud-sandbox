package org.jaweze.hello.ui.model;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.jaweze.hello.model.Customer;

@SpringComponent
@ViewScope
public class EditorModel {

    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
