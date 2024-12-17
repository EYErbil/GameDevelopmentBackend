package com.dreamgames.backendengineeringcasestudy.servicetest;

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

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * This test will run against the actual Spring Boot context,
 * assuming H2 in-memory DB and schema from mysql-db-dump.sql are set up.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // ensure that test profile is picked if needed
public class LeaderboardIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        // Create some users
        User u1 = new User(); u1.setAbGroup(AbGroup.A); u1.setLevel(10); u1.setCoins(2000);
        User u2 = new User(); u2.setAbGroup(AbGroup.B); u2.setLevel(50); u2.setCoins(5000);
        User u3 = new User(); u3.setAbGroup(AbGroup.A); u3.setLevel(100); u3.setCoins(8000);

        userRepository.saveAll(List.of(u1, u2, u3));
    }

    @Test
    void testLeaderboard() throws Exception {
        // After setup, we have users with levels: 10, 50, 100
        // The order should be by level desc: 100, 50, 10

        mockMvc.perform(get("/leaderboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entries[0].level").value(100))
                .andExpect(jsonPath("$.entries[1].level").value(50))
                .andExpect(jsonPath("$.entries[2].level").value(10));
    }
}
