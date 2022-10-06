package com.javachallenge.basico.controller;

import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired private UserRepository repository;

    @GetMapping("/")
    public ResponseEntity all() {
        List<User> users = repository.findAll();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping("registration")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        user.setRoles(Collections.singleton(User.Role.USER));
        repository.save(user);
        return user;
    }
}
