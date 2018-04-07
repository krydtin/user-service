package com.krydtin.user.services;

import com.krydtin.user.exceptions.DataNotFoundException;
import com.krydtin.user.model.User;
import com.krydtin.user.exceptions.RegistrationException;

public interface UserService {

    User createUser(User user) throws RegistrationException;

    User findByUsername(String username) throws DataNotFoundException;

    String signin(User user);
}
