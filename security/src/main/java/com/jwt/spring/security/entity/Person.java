package com.jwt.spring.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Person entity represents a user in the system with fields for id, username, password, and email.
 * It is mapped to a database table using JPA annotations.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    private Long id;
    private String userName;
    private String password;
    private String emailId;

}
