// src/main/java/com/dreamgames/backendengineeringcasestudy/dto/UpdateLevelResponse.java

package com.dreamgames.backendengineeringcasestudy.dto;

public class UpdateLevelResponse {
    private Integer userId;
    private int level;
    private int coins;
    private int helium; //will add helium, just polishing for now

    public UpdateLevelResponse() {}

    public UpdateLevelResponse(Integer userId, int level, int coins, int helium) {
        this.userId = userId;
        this.level = level;
        this.coins = coins;
        this.helium = helium;
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

    public int getHelium() {
        return helium;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setHelium(int helium) {
        this.helium = helium;
    }
}
