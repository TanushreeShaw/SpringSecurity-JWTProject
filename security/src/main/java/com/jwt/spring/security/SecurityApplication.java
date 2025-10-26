package com.jwt.spring.security;

import com.jwt.spring.security.dao.PersonDao;
import com.jwt.spring.security.entity.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SecurityApplication {

	private final PersonDao personDao;

	public SecurityApplication(PersonDao personDao) {
		this.personDao = personDao;
	}

	/*@PostConstruct
	public void init() {
		Person person = new Person(1L, "user1", "password1", "user1@example.com");
		personDao.save(person);
	}*/

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

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}
}
