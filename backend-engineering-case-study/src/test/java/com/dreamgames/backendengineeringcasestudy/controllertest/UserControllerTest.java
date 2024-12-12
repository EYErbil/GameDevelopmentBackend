package com.dreamgames.backendengineeringcasestudy.controllertest;

import com.dreamgames.backendengineeringcasestudy.controller.UserController;
import com.dreamgames.backendengineeringcasestudy.dto.CreateUserRequest;
import com.dreamgames.backendengineeringcasestudy.dto.CreateUserResponse;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    // Given: Mocked UserService to return a User object
    // When: UserController createUser method is called
    // Then: Verify that the returned CreateUserResponse object has the correct values
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testCreateUser() {
        // Given
        User user = new User();
        user.setId(1);
        user.setLevel(1);
        user.setCoins(2000);
        when(userService.createUser()).thenReturn(user);

        // When
        CreateUserResponse result = userController.createUser(null);

        // Then
        assertEquals(1, result.getUserId().intValue());
        assertEquals(1, result.getLevel());
        assertEquals(2000, result.getCoins());
    }
}