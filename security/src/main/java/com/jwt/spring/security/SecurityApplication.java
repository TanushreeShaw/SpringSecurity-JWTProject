package com.jwt.spring.security;

import com.jwt.spring.security.dao.PersonDao;
import com.jwt.spring.security.entity.Person;
import com.jwt.spring.security.service.PersonDetailsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootApplication
public class SecurityApplication {

	@Autowired
	private PersonDao personDao;

	@PostConstruct
	public void initPersonsList() {
		List<Person> personList = Stream.of(
				new Person(101, "root", "root", "anna.matthews@zoho.com"),
				new Person(102, "JohnDoe", "dfghjklpoiuytre", "john.doe@gmail.com"),
				new Person(103, "JaneSmith", "qwertyuiopasdfgh", "jana.smith@yahoo.com"),
				new Person(104, "MichaelJohnson", "zxcvbnmasdfghjkl", "michael.johnson@hotmail.com"),
				new Person(105, "EmilyDavis", "plmoknijbuhvygct", "emily.davis@outlook.com")
		).collect(Collectors.toList());

		personDao.saveAll(personList);
	}
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

}
