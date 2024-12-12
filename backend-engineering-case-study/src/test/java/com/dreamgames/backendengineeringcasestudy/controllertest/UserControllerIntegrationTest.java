// src/test/java/com/dreamgames/backendengineeringcasestudy/controller/UserControllerIntegrationTest.java

package com.dreamgames.backendengineeringcasestudy.controllertest;

import com.dreamgames.backendengineeringcasestudy.dto.UpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.entity.AbGroup;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper;
    private User userA;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Create and save a user for testing
        userA = new User();
        userA.setAbGroup(AbGroup.A);
        userA.setLevel(1);
        userA.setCoins(2000);
        userA.setHelium(0);
        userA = userRepository.save(userA);
        System.out.println("Initialized userA with level: " + userA.getLevel());
    }

    @Test
    void createUser_ShouldCreateAndReturnNewUser() throws Exception {
        // Arrange
        String emptyJson = "{}"; // Sending an empty JSON object

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.level").value(1))
                .andExpect(jsonPath("$.coins").value(2000))
                .andExpect(jsonPath("$.abGroup", anyOf(is("A"), is("B"))));
    }
    //Important thing about these tests, is that the json application does send an update, so we get +'s there, and since I know
    //the default values, I can just type in expected values. because userA.getLevel()+1 doesnt work, we basically double update it.
    @Test
    void updateLevel_ShouldUpdateUserLevelAndCoins() throws Exception {
        // Arrange
        UpdateLevelRequest request = new UpdateLevelRequest(userA.getId(), false);
        System.out.println("Before update: level=" + userA.getLevel());

        // Act & Assert
        mockMvc.perform(post("/users/updateLevel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userA.getId()))
                .andExpect(jsonPath("$.level").value(2)) // Expect level=2
                .andExpect(jsonPath("$.coins").value(2100))
                .andExpect(jsonPath("$.helium").value(0));

        // Fetch the user again to verify
        User updatedUser = userRepository.findById(userA.getId()).orElseThrow();
        System.out.println("After update: level=" + updatedUser.getLevel());
    }

    @Test
    void updateLevel_WithEventParticipation_ShouldIncreaseHelium() throws Exception {
        // Arrange
        UpdateLevelRequest request = new UpdateLevelRequest(userA.getId(), true);
        System.out.println("Before update with event: level=" + userA.getLevel());

        // Act & Assert
        mockMvc.perform(post("/users/updateLevel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userA.getId()))
                .andExpect(jsonPath("$.level").value(2)) // Expect level=2
                .andExpect(jsonPath("$.coins").value(2100))
                .andExpect(jsonPath("$.helium").value(10));

        // Fetch the user again to verify
        User updatedUser = userRepository.findById(userA.getId()).orElseThrow();
        System.out.println("After update with event: level=" + updatedUser.getLevel());
    }

    @Test
    void updateLevel_ShouldReturnBadRequest_WhenUserNotFound() throws Exception {
        // Arrange
        UpdateLevelRequest request = new UpdateLevelRequest(999, true);

        // Act & Assert
        mockMvc.perform(post("/users/updateLevel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("User with ID 999 not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

}
