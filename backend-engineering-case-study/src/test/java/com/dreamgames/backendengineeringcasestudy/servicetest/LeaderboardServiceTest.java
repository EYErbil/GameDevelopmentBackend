package com.dreamgames.backendengineeringcasestudy.servicetest;

import com.dreamgames.backendengineeringcasestudy.dto.LeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.entity.AbGroup;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.service.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaderboardServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LeaderboardService leaderboardService;

    @Test
    void testGetLeaderboard() {
        User user1 = new User();
        user1.setId(1);
        user1.setAbGroup(AbGroup.A);
        user1.setLevel(200);
        user1.setCoins(5000);

        User user2 = new User();
        user2.setId(2);
        user2.setAbGroup(AbGroup.B);
        user2.setLevel(150);
        user2.setCoins(4500);

        when(userRepository.findTop100ByOrderByLevelDesc()).thenReturn(List.of(user1, user2));

        LeaderboardResponse response = leaderboardService.getLeaderboard();

        assertNotNull(response);
        assertEquals(2, response.getEntries().size());
        assertEquals(1, response.getEntries().get(0).getUserId());
        assertEquals(200, response.getEntries().get(0).getLevel());
        assertEquals(2, response.getEntries().get(1).getUserId());
        assertEquals(150, response.getEntries().get(1).getLevel());

        verify(userRepository, times(1)).findTop100ByOrderByLevelDesc();
    }
}
