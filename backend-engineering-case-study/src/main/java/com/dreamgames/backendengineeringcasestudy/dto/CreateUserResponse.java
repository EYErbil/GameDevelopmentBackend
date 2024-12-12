package com.dreamgames.backendengineeringcasestudy.dto;

import com.dreamgames.backendengineeringcasestudy.entity.AbGroup;

public class CreateUserResponse {
    private Integer userId;
    private int level;
    private int coins;
    private AbGroup abGroup;

    public CreateUserResponse(Integer userId, int level, int coins, AbGroup abGroup) {
        this.userId = userId;
        this.level = level;
        this.coins = coins;
        this.abGroup = abGroup;
    }

    public Integer getUserId() {
        return userId;
    }

    public int getLevel() {
        return level;
    }

    public int getCoins() {
        return coins;
    }

    public AbGroup getAbGroup() {
        return abGroup;
    }
}