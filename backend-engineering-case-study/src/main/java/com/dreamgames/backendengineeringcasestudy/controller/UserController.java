package com.dreamgames.backendengineeringcasestudy.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import com.dreamgames.backendengineeringcasestudy.dto.CreateUserRequest;
import com.dreamgames.backendengineeringcasestudy.dto.CreateUserResponse;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Health check endpoint to verify if the application is running.
     *
     * @return A simple string indicating the application's status.
     */
    @GetMapping("/health")
    public String healthCheck() {
        return "Application is up and running!";
    }

    /**
     * Endpoint to create a new user.
     *
     * @param request The request body containing user creation details.
     * @return The response containing the newly created user's details.
     */
    @PostMapping
    public CreateUserResponse createUser(@RequestBody(required = false) CreateUserRequest request) {
        User newUser = userService.createUser();
        return new CreateUserResponse(
                newUser.getId(),
                newUser.getLevel(),
                newUser.getCoins(),
                newUser.getAbGroup()
        );
    }
}
