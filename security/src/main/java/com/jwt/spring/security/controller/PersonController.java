package com.jwt.spring.security.controller;

import com.jwt.spring.security.endpoint.PersonEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController implements PersonEndpoint {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to JWT Spring Security Example";
    }
}
