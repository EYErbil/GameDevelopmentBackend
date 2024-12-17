package com.dreamgames.backendengineeringcasestudy.dto;

import java.util.List;

public class LeaderboardResponse {

    private List<LeaderboardEntry> entries;

    public static class LeaderboardEntry {
        private Integer userId;
        private int level;

        public LeaderboardEntry(Integer userId, int level) {
            this.userId = userId;
            this.level = level;
        }

        public Integer getUserId() {
            return userId;
        }

        public int getLevel() {
            return level;
        }
    }

    public LeaderboardResponse(List<LeaderboardEntry> entries) {
        this.entries = entries;
    }

    public List<LeaderboardEntry> getEntries() {
        return entries;
    }
}
