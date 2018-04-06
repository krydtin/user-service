package com.krydtin.user.services.impl;

import com.krydtin.user.model.User;
import com.krydtin.user.repositories.UserRepository;
import com.krydtin.user.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public User createUser(User user) {
        final User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            //TODO throw Duplicated
        }
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            //TODO throw DataNotFound
        }
        return user;
    }

    @Override
    public String signin(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
