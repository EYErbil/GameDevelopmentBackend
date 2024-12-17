package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.LeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private final UserRepository userRepository;

    public LeaderboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LeaderboardResponse getLeaderboard() {
        List<User> topUsers = userRepository.findTop100ByOrderByLevelDesc();
        List<LeaderboardResponse.LeaderboardEntry> entries = topUsers.stream()
                .map(u -> new LeaderboardResponse.LeaderboardEntry(u.getId(), u.getLevel()))
                .collect(Collectors.toList());

        return new LeaderboardResponse(entries);
    }
}
