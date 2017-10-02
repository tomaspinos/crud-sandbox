package org.jaweze.hello;

import org.jaweze.hello.model.Customer;
import org.jaweze.hello.model.MarriageStatus;
import org.jaweze.hello.model.Sex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

import java.time.LocalDate;
import java.time.Month;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(CustomerRepository repository) {
		return (args) -> {
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


		};
	}

}
