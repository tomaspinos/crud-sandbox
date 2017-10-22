package org.jaweze.hello;

import org.jaweze.hello.model.Customer;
import org.jaweze.hello.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/customers")
    public List<Customer> find(@RequestParam(required = false) String lastName) {
        if (StringUtils.hasText(lastName)) {
            return repository.findByLastNameStartsWithIgnoreCase(lastName);
        } else {
            return repository.findAll();
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Customer> findOne(@PathVariable long customerId) {
        Customer customer = repository.findOne(customerId);
        return customer != null ?
                new ResponseEntity<>(customer, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/customer")
    public Customer create(@RequestBody Customer customer) {
        return repository.save(customer);
    }

    @PutMapping("/customer")
    public Customer update(@RequestBody Customer customer) {
        return repository.save(customer);
    }

    @DeleteMapping("/customer/{customerId}")
    public void delete(@PathVariable long customerId) {
        repository.delete(customerId);
    }
}
