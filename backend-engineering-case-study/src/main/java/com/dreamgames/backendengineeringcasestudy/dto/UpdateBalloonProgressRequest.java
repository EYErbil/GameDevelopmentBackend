package com.dreamgames.backendengineeringcasestudy.dto;

public class UpdateBalloonProgressRequest {
    private Integer userId;
    private int heliumToUse;

    public Integer getUserId() { return userId; }
    public int getHeliumToUse() { return heliumToUse; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public void setHeliumToUse(int heliumToUse) { this.heliumToUse = heliumToUse; }
}