package com.krydtin.user.services;

import com.krydtin.user.configurations.jwt.JwtTokenProvider;
import com.krydtin.user.constant.ErrorCode;
import com.krydtin.user.constant.MemberType;
import com.krydtin.user.exceptions.AuthenticationException;
import com.krydtin.user.exceptions.DataNotFoundException;
import com.krydtin.user.exceptions.RegistrationException;
import com.krydtin.user.model.User;
import com.krydtin.user.repositories.UserRepository;
import com.krydtin.user.services.impl.UserServiceImpl;
import com.krydtin.user.utils.DateUtils;
import static com.krydtin.user.utils.TestUtils.createUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    public UserServiceTest() {
    }

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private User mockUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl();
        userService.setUserRepository(userRepository);
        userService.setbCryptPasswordEncoder(bCryptPasswordEncoder);
        userService.setAuthenticationManager(authenticationManager);
        userService.setJwtTokenProvider(jwtTokenProvider);
    }

    @Test
    public void shouldReturnUserWhenFindByUsername() throws DataNotFoundException {
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);

        final User user = userService.findByUsername("testUser");

        assertThat(user, is(equalTo(mockUser)));
    }

    @Test(expected = DataNotFoundException.class)
    public void shouldThrowDataNotFoundExceptionWhenFindByUsername() throws DataNotFoundException {
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);

        userService.findByUsername("testUserx");
    }

    @Test
    public void shouldVerifyThatSaveMethodIsCalledWhenCreateUser() throws RegistrationException {
        final User user = createUser();
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        userService.createUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldSetMemberTypeToSilverWhenCreateUser() throws RegistrationException {
        final User user = createUser();
        user.setSalary(15_000d);
        userService.createUser(user);
        assertThat(user.getMemberType(), is(equalTo(MemberType.Silver)));
    }

    @Test
    public void shouldSetMemberTypeToGoldWhenCreateUser() throws RegistrationException {
        final User user = createUser();
        user.setSalary(30_000d);
        userService.createUser(user);
        assertThat(user.getMemberType(), is(equalTo(MemberType.Gold)));
    }

    @Test
    public void shouldSetMemberTypeToPlatinumWhenCreateUser() throws RegistrationException {
        final User user = createUser();
        user.setSalary(50_001d);
        userService.createUser(user);
        assertThat(user.getMemberType(), is(equalTo(MemberType.Platinum)));
    }

    @Test
    public void shouldThrowRegistrationExceptionWhenCreateUser() throws RegistrationException {
        final User user = createUser();
        user.setSalary(14_000d);
        try {
            userService.createUser(user);
            fail();
        } catch (RegistrationException e) {
            assertThat(e.getErrorCode(), is(equalTo(ErrorCode.Registration.MINIMUM_INCOME)));
        }
    }

    @Test
    public void shouldSetReferenCodeWhenCreateUser() throws RegistrationException {
        final User user = createUser();
        user.setPhone("0861112345");
        userService.createUser(user);
        assertThat(user.getReferenceCode(), is(equalTo(DateUtils.currentDate() + "2345")));
    }

    @Test
    public void shouldThrowRegistrationExceptionWhenCreateDuplicateUser() throws RegistrationException {
        final User user = createUser();
        try {
            when(userRepository.findByUsername(anyString())).thenReturn(user);
            userService.createUser(user);
            fail();
        } catch (RegistrationException e) {
            assertThat(e.getErrorCode(), is(equalTo(ErrorCode.Registration.DUPLICATE_USERNAME)));
        }
    }

    @Test
    public void shouldReturnTokenWhenSigninSuccess() {
        when(jwtTokenProvider.createToken(anyString())).thenReturn("token");
        final String token = userService.signin(mockUser);
        assertThat(token, is(equalTo("token")));
    }
    
    @Test(expected = AuthenticationException.class)
    public void shouldThrowAuthenticationExceptionWhenSignin() {
        when(authenticationManager.authenticate(any())).thenThrow(new org.springframework.security.core.AuthenticationException("") {
        });
        userService.signin(mockUser);
    }

}
