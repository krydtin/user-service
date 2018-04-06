package com.krydtin.user.controller;

import com.krydtin.user.model.User;
import com.krydtin.user.services.UserService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signin")
    public String login(@RequestBody User user) {
        return userService.signin(user);
    }

    @PostMapping(value = "/signup")
    @ResponseStatus(value = HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) { 
        return userService.createUser(user);
    }

    @GetMapping(value = "/users/{username}")
    public User getUser(@PathVariable(required = true, name = "username") String username) {
        return userService.findByUsername(username);
    }

}
