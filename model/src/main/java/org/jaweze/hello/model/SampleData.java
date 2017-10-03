package org.jaweze.hello.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Month;

@Component
public class SampleData {

    private final CustomerRepository repository;

    private static final Logger log = LoggerFactory.getLogger(SampleData.class);

    @Autowired
    public SampleData(CustomerRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initialize() {
        // save a couple of customers
        repository.save(new Customer("Jack", "Bauer", LocalDate.of(1970, Month.JANUARY, 1), Sex.MALE, MarriageStatus.SINGLE));
        repository.save(new Customer("Chloe", "O'Brian", LocalDate.of(1971, Month.FEBRUARY, 3), Sex.FEMALE, MarriageStatus.SINGLE));
        repository.save(new Customer("Kim", "Bauer", LocalDate.of(1972, Month.MARCH, 5), Sex.FEMALE, MarriageStatus.MARRIED));
        repository.save(new Customer("David", "Palmer", LocalDate.of(1973, Month.APRIL, 7), Sex.MALE, MarriageStatus.DIVORCED));
        repository.save(new Customer("Michelle", "Dessler", LocalDate.of(1974, Month.MAY, 9), Sex.FEMALE, MarriageStatus.WIDOWED));

        // fetch all customers
        log.info("Customers found with findAll():");
        log.info("-------------------------------");
        for (Customer customer : repository.findAll()) {
            log.info(customer.toString());
        }
        log.info("");

        // fetch an individual customer by ID
        Customer customer = repository.findOne(1L);
        log.info("Customer found with findOne(1L):");
        log.info("--------------------------------");
        log.info(customer.toString());
        log.info("");

        // fetch customers by last name
        log.info("Customer found with findByLastNameStartsWithIgnoreCase('Bauer'):");
        log.info("--------------------------------------------");
        for (Customer bauer : repository.findByLastNameStartsWithIgnoreCase("Bauer")) {
            log.info(bauer.toString());
        }
        log.info("");
    }
}
