package com.dreamgames.backendengineeringcasestudy.controllertest;

import com.dreamgames.backendengineeringcasestudy.controller.LeaderboardController;
import com.dreamgames.backendengineeringcasestudy.dto.LeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.service.LeaderboardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LeaderboardController.class)
public class LeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaderboardService leaderboardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetLeaderboard() throws Exception {
        LeaderboardResponse.LeaderboardEntry entry1 = new LeaderboardResponse.LeaderboardEntry(1, 200);
        LeaderboardResponse.LeaderboardEntry entry2 = new LeaderboardResponse.LeaderboardEntry(2, 150);
        LeaderboardResponse response = new LeaderboardResponse(List.of(entry1, entry2));

        when(leaderboardService.getLeaderboard()).thenReturn(response);

        mockMvc.perform(get("/leaderboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entries[0].userId").value(1))
                .andExpect(jsonPath("$.entries[0].level").value(200))
                .andExpect(jsonPath("$.entries[1].userId").value(2))
                .andExpect(jsonPath("$.entries[1].level").value(150));

        verify(leaderboardService, times(1)).getLeaderboard();
    }
}
