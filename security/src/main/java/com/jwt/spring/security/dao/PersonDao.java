package com.jwt.spring.security.dao;

import com.jwt.spring.security.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonDao extends JpaRepository<Person, Integer> {
    Optional<Person> findByUserName(String userName);
}
