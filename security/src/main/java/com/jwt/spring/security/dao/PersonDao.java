package com.jwt.spring.security.dao;

import com.jwt.spring.security.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * PersonDao is a Data Access Object (DAO) interface for managing Person entities.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface PersonDao extends JpaRepository<Person, Long> {

    Optional<Person> findByUserName(String userName);

}
