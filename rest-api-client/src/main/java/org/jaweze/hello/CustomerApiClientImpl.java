package org.jaweze.hello;

import org.jaweze.hello.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class CustomerApiClientImpl implements CustomerApiClient {

    private final RestTemplate restTemplate;
    private final String url;

    private static final Logger logger = LoggerFactory.getLogger(CustomerApiClientImpl.class);

    public CustomerApiClientImpl() {
        restTemplate = new RestTemplate();
        url = "http://localhost:8081/crud";
    }

    @Override
    public Customer create(Customer customer) {
        return restTemplate.postForObject(url + "/customer", customer, Customer.class);
    }

    @Override
    public Optional<Customer> getById(long customerId) {
        try {
            return Optional.of(restTemplate.getForObject(url + "/customer/" + customerId, Customer.class));
        } catch (HttpStatusCodeException e) {
            logger.error("Error while getting customer by id {}", customerId, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Customer> getAll(String lastName) {
        ResponseEntity<Customer[]> responseEntity = restTemplate.getForEntity(url + "/customers", Customer[].class,
                new HashMap<String, String>() {{ put("lastName", lastName); }});
        Customer[] customers = responseEntity.getBody();
        return customers != null ? Arrays.asList(customers) : Collections.emptyList();
    }

    @Override
    public void update(Customer customer) {
        restTemplate.put(url + "/customer", customer);
    }

    @Override
    public void delete(long customerId) {
        restTemplate.delete(url + "/customer/" + customerId);
    }

    @Override
    public void delete(Customer customer) {
        delete(customer.getId());
    }
}
