package com.jwt.spring.security.service;

import com.jwt.spring.security.dao.PersonDao;
import com.jwt.spring.security.entity.Person;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * PersonDetailsServiceImpl is an implementation of UserDetailsService
 * that loads user-specific data from the PersonDao.
 */
@Service
public class PersonDetailsServiceImpl implements UserDetailsService {

    private final PersonDao personDao;

    public PersonDetailsServiceImpl(PersonDao personDao) {
        this.personDao = personDao;
    }

    /**
     * Loads the user by username and constructs a UserDetails object.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated UserDetails object (never null)
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personDao.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(person.getUserName())
                .password(person.getPassword())
                .roles("USER")
                .build();
    }
}
