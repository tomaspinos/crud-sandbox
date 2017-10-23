package org.jaweze.hello.ui.model;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.jaweze.hello.model.Customer;

import java.io.Serializable;
import java.util.List;

@SpringComponent
@ViewScope
public class GridModel implements Serializable {

    private List<Customer> customers;

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
