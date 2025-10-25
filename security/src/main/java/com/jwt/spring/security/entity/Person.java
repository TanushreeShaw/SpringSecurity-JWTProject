package com.jwt.spring.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PERSON_MASTER")
public class Person {

    @Id
    private int personId;

    private String userName;
    private String password;
    private String emailId;

}
