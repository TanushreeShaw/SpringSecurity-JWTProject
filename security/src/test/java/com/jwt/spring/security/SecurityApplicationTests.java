package com.jwt.spring.security;

import com.jwt.spring.security.dao.PersonDao;
import com.jwt.spring.security.entity.Person;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the SecurityApplication class.
 * These tests cover the initialization of the persons list and the main method execution.
 */
@SpringBootTest
class SecurityApplicationTests {

	/**
	 * Test to ensure the application context loads successfully.
	 */
	@Test
	void contextLoads() {
	}

	/**
	 * Test the initPersonsList method to verify it saves the correct sample persons.
	 */
	@Test
	void testInitPersonsList_savesSamplePersons() {
		PersonDao personDao = mock(PersonDao.class);
		SecurityApplication securityApplication = new SecurityApplication(personDao);

		securityApplication.initPersonsList();

		ArgumentCaptor<List<Person>> argumentCaptor = ArgumentCaptor.forClass(List.class);
		verify(personDao, times(1)).saveAll(argumentCaptor.capture());

		List<Person> savedPersons = argumentCaptor.getValue();
		assertEquals(5, savedPersons.size());
		assertTrue(savedPersons.stream().anyMatch(person -> "AnnaMatthews".equals(person.getUserName())));
		assertTrue(savedPersons.stream().anyMatch(person -> "JohnDoe".equals(person.getUserName())));
		assertTrue(savedPersons.stream().anyMatch(person -> "JaneSmith".equals(person.getUserName())));
		assertTrue(savedPersons.stream().anyMatch(person -> "MichaelJohnson".equals(person.getUserName())));
		assertTrue(savedPersons.stream().anyMatch(person -> "EmilyDavis".equals(person.getUserName())));
	}

	/**
	 * Test the main method to ensure the application starts without errors.
	 */
	@Test
	void testMainMethod_runsApplication() {
		SecurityApplication.main(new String[]{});
	}

}
