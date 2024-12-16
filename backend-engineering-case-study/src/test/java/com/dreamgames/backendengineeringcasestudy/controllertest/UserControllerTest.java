// src/test/java/com/dreamgames/backendengineeringcasestudy/controller/UserControllerTest.java

package com.dreamgames.backendengineeringcasestudy.controllertest;

import com.dreamgames.backendengineeringcasestudy.controller.UserController;
import com.dreamgames.backendengineeringcasestudy.dto.CreateUserRequest;
import com.dreamgames.backendengineeringcasestudy.dto.CreateUserResponse;
import com.dreamgames.backendengineeringcasestudy.dto.UpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.dto.UpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.entity.AbGroup;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.repository.PopBalloonEventInvitationRepository;
import com.dreamgames.backendengineeringcasestudy.repository.PopBalloonEventParticipationRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;




    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;
    private User userA;
    private User userB;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        userA = new User();
        userA.setId(1);
        userA.setAbGroup(AbGroup.A);
        userA.setLevel(1);
        userA.setCoins(2000);

        userB = new User();
        userB.setId(2);
        userB.setAbGroup(AbGroup.B);
        userB.setLevel(5);
        userB.setCoins(5000);

    }

    @Test
    void healthCheck_ShouldReturnStatusMessage() throws Exception {
        mockMvc.perform(get("/users/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Application is up and running!"));
    }

    @Test
    void createUser_ShouldReturnCreateUserResponse() throws Exception {
        // Arrange
        CreateUserResponse response = new CreateUserResponse(userA.getId(), userA.getLevel(), userA.getCoins(), userA.getAbGroup());
        when(userService.createUser()).thenReturn(userA);

        CreateUserRequest request = new CreateUserRequest(); // Currently empty

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userA.getId()))
                .andExpect(jsonPath("$.level").value(userA.getLevel()))
                .andExpect(jsonPath("$.coins").value(userA.getCoins()))
                .andExpect(jsonPath("$.abGroup").value(userA.getAbGroup().toString()));

        verify(userService, times(1)).createUser();
    }

    @Test
    void updateLevel_ShouldReturnUpdatedUserProgress() throws Exception {
        // Arrange
        UpdateLevelRequest request = new UpdateLevelRequest(userA.getId(), false);
        UpdateLevelResponse response = new UpdateLevelResponse(userA.getId(), 2, 2100, 0);

        when(userService.updateLevel(ArgumentMatchers.any(UpdateLevelRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/users/updateLevel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(response.getUserId()))
                .andExpect(jsonPath("$.level").value(response.getLevel()))
                .andExpect(jsonPath("$.coins").value(response.getCoins()))
                .andExpect(jsonPath("$.helium").value(response.getHelium()));

        verify(userService, times(1)).updateLevel(any(UpdateLevelRequest.class));
    }

    @Test
    void updateLevel_ShouldReturnUpdatedUserProgress_WithHelium() throws Exception {
        // Arrange
        UpdateLevelRequest request = new UpdateLevelRequest(userB.getId(), true);
        UpdateLevelResponse response = new UpdateLevelResponse(userB.getId(), 6, 5100, 30);

        when(userService.updateLevel(ArgumentMatchers.any(UpdateLevelRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/users/updateLevel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(response.getUserId()))
                .andExpect(jsonPath("$.level").value(response.getLevel()))
                .andExpect(jsonPath("$.coins").value(response.getCoins()))
                .andExpect(jsonPath("$.helium").value(response.getHelium()));

        verify(userService, times(1)).updateLevel(any(UpdateLevelRequest.class));
    }

    @Test
    void createUser_ShouldHandleServiceException() throws Exception {
        // Arrange
        when(userService.createUser()).thenThrow(new IllegalStateException("User creation failed"));

        CreateUserRequest request = new CreateUserRequest(); // Currently empty

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("User creation failed"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(userService, times(1)).createUser();
    }

    @Test
    void updateLevel_ShouldHandleUserNotFoundException() throws Exception {
        // Arrange
        UpdateLevelRequest request = new UpdateLevelRequest(999, true);
        when(userService.updateLevel(any(UpdateLevelRequest.class))).thenThrow(new IllegalArgumentException("User with ID 999 not found"));

        // Act & Assert
        mockMvc.perform(post("/users/updateLevel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("User with ID 999 not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(userService, times(1)).updateLevel(any(UpdateLevelRequest.class));
    }


}
