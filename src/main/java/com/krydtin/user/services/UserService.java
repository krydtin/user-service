package com.krydtin.user.services;

import com.krydtin.user.model.User;

public interface UserService {

    User createUser(User user);

    User findByUsername(String username);

    String signin(User user);
}
