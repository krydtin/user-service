package com.krydtin.user.services.impl;

import com.krydtin.user.configurations.jwt.JwtTokenProvider;
import com.krydtin.user.model.User;
import com.krydtin.user.repositories.UserRepository;
import com.krydtin.user.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User createUser(User user) {
        final User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            //TODO throw Duplicated
            log.error("Duplicated");
        }
        //TODO check salary and set member type, reference code YYYYMMDD + phone last 4 digit 
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            //TODO throw DataNotFound
            log.error("Not found");
        }
        return user;
    }

    @Override
    public String signin(User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            return jwtTokenProvider.createToken(user.getUsername());
        } catch (AuthenticationException e) {
            //TODO catch and throw custom exception
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
}
