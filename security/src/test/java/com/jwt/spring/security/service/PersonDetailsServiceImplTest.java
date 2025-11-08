package com.jwt.spring.security.service;

import com.jwt.spring.security.dao.PersonDao;
import com.jwt.spring.security.entity.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the PersonDetailsServiceImpl class.
 * These tests cover loading user details by username.
 */
class PersonDetailsServiceImplTest {

    private PersonDao personDao;
    private PersonDetailsServiceImpl service;

    @BeforeEach
    void setUp() {
        personDao = mock(PersonDao.class);
        service = new PersonDetailsServiceImpl(personDao);
    }

    /**
     * Test loading user details when the user is found.
     */
    @Test
    void testLoadUserByUsernameUserFound() {
        Person person = new Person();
        person.setUserName("testuser");
        person.setPassword("testpass");

        when(personDao.findByUserName("testuser")).thenReturn(Optional.of(person));

        UserDetails userDetails = service.loadUserByUsername("testuser");

        assertEquals("testuser", userDetails.getUsername());
        assertEquals("testpass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    /**
     * Test loading user details when the user is not found.
     */
    @Test
    void testLoadUserByUsernameUserNotFound() {
        when(personDao.findByUserName("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("unknown"));
    }
}