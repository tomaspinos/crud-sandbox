package org.jaweze.hello;

import org.jaweze.hello.model.Customer;
import org.jaweze.hello.model.MarriageStatus;
import org.jaweze.hello.model.Sex;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RestApiClientApplication {

    public static void main(String[] args) {
        CustomerApiClientImpl client = new CustomerApiClientImpl();

        Optional<Customer> maybeCustomer = client.getById(1);
        System.out.println("customer = " + maybeCustomer.orElse(null));

        Customer newCustomer = client.create(new Customer("a", "a", LocalDate.now(), Sex.OTHER, MarriageStatus.OTHER));
        System.out.println("new customer = " + newCustomer);

        newCustomer.setLastName("b");
        client.update(newCustomer);

        client.delete(newCustomer.getId());

        List<Customer> customers = client.getAll("Bauer");
        System.out.println("customers = " + customers);
    }
}
