package org.jaweze.hello;

import org.jaweze.hello.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerApiClient {

    Customer create(Customer customer);

    Optional<Customer> getById(long customerId);

    List<Customer> getAll(String lastName);

    void update(Customer customer);

    void delete(long customerId);

    void delete(Customer customer);
}
