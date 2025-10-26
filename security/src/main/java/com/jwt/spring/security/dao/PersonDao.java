package com.jwt.spring.security.dao;

import com.jwt.spring.security.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonDao extends JpaRepository<Person, Long> {
    Optional<Person> findByUserName(String userName);
}

