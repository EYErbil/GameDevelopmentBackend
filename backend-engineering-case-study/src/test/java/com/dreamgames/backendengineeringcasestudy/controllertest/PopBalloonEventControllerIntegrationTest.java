package com.dreamgames.backendengineeringcasestudy.controllertest;

import com.dreamgames.backendengineeringcasestudy.entity.*;
import com.dreamgames.backendengineeringcasestudy.repository.*;
import com.dreamgames.backendengineeringcasestudy.service.PopBalloonEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PopBalloonEventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PopBalloonEventParticipationRepository participationRepository;

    @SpyBean
    private PopBalloonEventService eventService;

    @BeforeEach
    void setUp() {
        // Insert a user that meets the requirement
        User user = new User();
        user.setAbGroup(AbGroup.A);
        user.setLevel(50);
        user.setCoins(3000);
        userRepository.save(user);

        // Force event active by stubbing isEventActive() to return true
        doReturn(true).when(eventService).isEventActive();
    }

    @Test
    void joinEvent_ShouldCreateParticipationRecord() throws Exception {
        // Find user
        User user = userRepository.findAll().get(0);

        // Perform the join event request
        mockMvc.perform(post("/pop-balloon-event/join")
                        .param("userId", String.valueOf(user.getId())))
                .andExpect(status().isOk()); // Expecting HTTP 200

        // Verify that a participation record was created
        List<PopBalloonEventParticipation> parts = participationRepository.findAll();
        assertEquals(1, parts.size(), "Participation record should be created");
        assertEquals(user.getId(), parts.get(0).getUserId(), "Participation should be linked to the correct user");
    }
}
