package com.krydtin.user.services.impl;

import com.krydtin.user.configurations.jwt.JwtTokenProvider;
import com.krydtin.user.constant.ErrorCode;
import com.krydtin.user.constant.MemberType;
import com.krydtin.user.exceptions.DataNotFoundException;
import com.krydtin.user.model.User;
import com.krydtin.user.repositories.UserRepository;
import com.krydtin.user.services.UserService;
import com.krydtin.user.exceptions.RegistrationException;
import static com.krydtin.user.utils.DateUtils.currentDate;
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
    public User findByUsername(String username) throws DataNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new DataNotFoundException(ErrorCode.DataNotFound.DATA_NOT_FOUND, "User '" + username + "' not found");
        }
        return user;
    }

    @Override
    public User createUser(User user) throws RegistrationException {
        final User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RegistrationException(ErrorCode.Registration.DUPLICATE_USERNAME, "Username '" + user.getUsername() + "' is already exist");
        }
        setMemberType(user);
        setReferenCode(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public String signin(User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            return jwtTokenProvider.createToken(user.getUsername());
        } catch (AuthenticationException e) {
            throw new com.krydtin.user.exceptions.AuthenticationException(ErrorCode.Authentication.INVALID_USERNAME_PASSWORD, "Invalid username or password");
        }
    }

    private void setMemberType(User user) throws RegistrationException {
        final double salary = user.getSalary();
        if (salary > 50_000) {
            user.setMemberType(MemberType.Platinum);
        } else if (salary >= 30_000) {
            user.setMemberType(MemberType.Gold);
        } else if (salary >= 15_000) {
            user.setMemberType(MemberType.Silver);
        } else {
            throw new RegistrationException(ErrorCode.Registration.MINIMUM_INCOME, "Salary don't meet minimum requirements");
        }
    }

    private void setReferenCode(User user) {
        final String phone = user.getPhone();
        user.setReferenceCode(currentDate() + phone.substring(phone.length() - 4, phone.length()));
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
