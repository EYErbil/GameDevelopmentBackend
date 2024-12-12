package com.dreamgames.backendengineeringcasestudy.servicetest;

import com.dreamgames.backendengineeringcasestudy.entity.AbGroup;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    // Given: Mocked UserRepository to return a User object
    // When: UserService createUser method is called
    // Then: Verify that the returned User object is not null
    @Test
    void testCreateUser() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // When
        User user = userService.createUser();

        // Then
        assertNotNull(user);
    }
}