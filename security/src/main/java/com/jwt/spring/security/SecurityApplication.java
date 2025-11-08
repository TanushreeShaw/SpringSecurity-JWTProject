package com.jwt.spring.security;

import com.jwt.spring.security.dao.PersonDao;
import com.jwt.spring.security.entity.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SecurityApplication is the main entry point for the Spring Boot application.
 * It initializes the application context and populates the database with sample Person data.
 */
@SpringBootApplication
public class SecurityApplication {

	private final PersonDao personDao;

	public SecurityApplication(PersonDao personDao) {
		this.personDao = personDao;
	}

	/**
	 * Initializes the database with a list of sample Person entities after the application starts.
	 * This method is annotated with @PostConstruct to ensure it runs after dependency injection is complete.
	 */
	@PostConstruct
	public void initPersonsList() {
		List<Person> personList = Stream.of(
				new Person(101L, "AnnaMatthews", "exrdtcyguhbijnk", "anna.matthews@zoho.com"),
				new Person(102L, "JohnDoe", "dfghjklpoiuytre", "john.doe@gmail.com"),
				new Person(103L, "JaneSmith", "qwertyuiopasdfgh", "jana.smith@yahoo.com"),
				new Person(104L, "MichaelJohnson", "zxcvbnmasdfghjkl", "michael.johnson@hotmail.com"),
				new Person(105L, "EmilyDavis", "plmoknijbuhvygct", "emily.davis@outlook.com")
		).collect(Collectors.toList());

		personDao.saveAll(personList);
	}

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}
}
