package com.krydtin.user.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krydtin.user.constant.ErrorCode;
import com.krydtin.user.exceptions.AuthenticationException;
import com.krydtin.user.exceptions.DataNotFoundException;
import com.krydtin.user.model.User;
import com.krydtin.user.services.UserService;
import static com.krydtin.user.utils.TestUtils.createUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.core.Is.is;
import org.junit.Before;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(secure = false, controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    private ObjectMapper objectMapper;

     @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }
    
    @Test
    public void shouldReturnUserWhenGetUser() throws DataNotFoundException, Exception {
        final User user = createUser();
        when(userService.findByUsername("testUser")).thenReturn(user);

        final ResultActions result = mockMvc.perform(get("/users/{username}", "testUser"));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.username", is("testUser")));
    }
    
    @Test
    public void shouldReturnStatusNoContentWhenGetUser() throws DataNotFoundException, Exception {
        when(userService.findByUsername("testUser")).thenThrow(new DataNotFoundException(ErrorCode.DataNotFound.DATA_NOT_FOUND, ""));

        final ResultActions result = mockMvc.perform(get("/users/{username}", "testUser"));

        result.andExpect(status().isNoContent());
    }
    
    @Test
    public void shouldReturnStatusOkWhenSignin() throws Exception {
        final User user = createUser();
        when(userService.signin(user)).thenReturn("qwertyuiop");

        final ResultActions result = mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        result.andExpect(status().isOk());
    }
    
    @Test
    public void shouldReturnStatusUnauthorizedWhenSignin() throws Exception {
        final User user = createUser();
        when(userService.signin(any(User.class))).thenThrow(new AuthenticationException(ErrorCode.Authentication.INVALID_USERNAME_PASSWORD, ""));

        final ResultActions result = mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        result.andExpect(status().isUnauthorized());
    }
    
    @Test
    public void shouldReturnStatusOkWhenSignup() throws Exception {
        final User user = createUser();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        
        when(userService.createUser(user)).thenReturn(user);
        
        final ResultActions result = mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        result.andExpect(status().isCreated());
    }
    
    @Test
    public void shouldReturnUnprocessableEntityWhenSignup() throws Exception {
        final User user = createUser();
        user.setUsername("test");
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, true);
        
        when(userService.createUser(user)).thenReturn(user);
        
        final ResultActions result = mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        result.andExpect(status().isUnprocessableEntity());
    }

}
