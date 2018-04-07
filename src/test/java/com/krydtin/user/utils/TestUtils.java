package com.krydtin.user.utils;

import com.krydtin.user.model.User;

public class TestUtils {

    public static User createUser() {
        return User.builder()
                    .username("testUser")
                    .password("complexPassword")
                    .address("Bangkok, Thailand")
                    .phone("0861234567")
                    .salary(20000d)
                .build();
    }
}
