// src/test/java/com/dreamgames/backendengineeringcasestudy/controller/UserControllerIntegrationTest.java

package com.dreamgames.backendengineeringcasestudy.controllertest;

import com.dreamgames.backendengineeringcasestudy.dto.UpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.entity.AbGroup;
import com.dreamgames.backendengineeringcasestudy.entity.PopBalloonEventParticipation;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.repository.PopBalloonEventParticipationRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.service.PopBalloonEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private PopBalloonEventService eventService; // Use @SpyBean to spy on Spring-managed bean

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PopBalloonEventParticipationRepository participationRepository;

    private ObjectMapper objectMapper;
    private User userA;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        userA = new User();
        userA.setAbGroup(AbGroup.A);
        userA.setLevel(1);
        userA.setCoins(2000);
        userA = userRepository.save(userA);
        System.out.println("Initialized userA with level: " + userA.getLevel());

        doReturn(true).when(eventService).isEventActive();
    }

    @Test
    void createUser_ShouldCreateAndReturnNewUser() throws Exception {
        String emptyJson = "{}"; // Sending an empty JSON object

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.level").value(1))
                .andExpect(jsonPath("$.coins").value(2000))
                .andExpect(jsonPath("$.abGroup", anyOf(is("A"), is("B"))));
    }

    /**
     * This test updates the user's level without event participation.
     * It checks if the level increments by 1 and coins increase by 100.
     */
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
                .andExpect(jsonPath("$.coins").value(2100)) // 2000 + 100
                .andExpect(jsonPath("$.helium").value(0)); // No event participation

        User updatedUser = userRepository.findById(userA.getId()).orElseThrow();
        System.out.println("After update: level=" + updatedUser.getLevel());
    }

    /**
     * This test updates the user's level with event participation.
     * It checks if the level increments by 1, coins are adjusted correctly,
     * and helium increases by 10.
     */
    @Test
    void updateLevel_WithEventParticipation_ShouldIncreaseHelium() throws Exception {
        // Arrange: Set userA to meet event participation requirements
        userA.setLevel(50);
        userA.setCoins(3000);
        userRepository.save(userA); // Persist the changes

        // Act: Perform the join event request
        mockMvc.perform(post("/pop-balloon-event/join")
                        .param("userId", String.valueOf(userA.getId())))
                .andExpect(status().isOk());

        // Verify that a participation record exists
        PopBalloonEventParticipation participation = participationRepository.findByUserIdAndEventDate(
                        userA.getId(),
                        LocalDate.now(ZoneOffset.UTC))
                .orElseThrow(() -> new AssertionError("Participation record not found after joining event"));
        System.out.println("Participation record found for userA: HeliumCollected=" + participation.getHeliumCollected());

        // Create UpdateLevelRequest with event participation flag set to true
        UpdateLevelRequest request = new UpdateLevelRequest(userA.getId(), true);
        System.out.println("Before update with participation: level=" + userA.getLevel() + ", coins=" + userA.getCoins());

        // Act & Assert
        mockMvc.perform(post("/users/updateLevel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userA.getId()))
                .andExpect(jsonPath("$.level").value(51)) // level increments by 1
                .andExpect(jsonPath("$.coins").value(600)) // 3000 - 2500 + 100
                .andExpect(jsonPath("$.helium").value(10)); // helium increases by 10

        // Fetch test
        User updatedUser = userRepository.findById(userA.getId()).orElseThrow();
        System.out.println("After update with participation: level=" + updatedUser.getLevel() + ", coins=" + updatedUser.getCoins());

        // Lets see if Helium changes
        PopBalloonEventParticipation updatedParticipation = participationRepository.findByUserIdAndEventDate(
                        userA.getId(),
                        LocalDate.now(ZoneOffset.UTC))
                .orElseThrow(() -> new AssertionError("Participation record not found after level update"));
        assertEquals(10, updatedParticipation.getHeliumCollected(), "Helium should be increased accordingly");
    }

    @Test
    void updateLevel_ShouldReturnBadRequest_WhenUserNotFound() throws Exception {
        UpdateLevelRequest request = new UpdateLevelRequest(999, true);

        mockMvc.perform(post("/users/updateLevel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("User with ID 999 not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

}
