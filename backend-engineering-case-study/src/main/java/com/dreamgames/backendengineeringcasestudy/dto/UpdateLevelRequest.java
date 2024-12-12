// src/main/java/com/dreamgames/backendengineeringcasestudy/dto/UpdateLevelRequest.java

package com.dreamgames.backendengineeringcasestudy.dto;

public class UpdateLevelRequest {
    private Integer userId;
    private boolean joinedPopTheBalloonEvent;

    public UpdateLevelRequest() {}

    public UpdateLevelRequest(Integer userId, boolean joinedPopTheBalloonEvent) {
        this.userId = userId;
        this.joinedPopTheBalloonEvent = joinedPopTheBalloonEvent;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isJoinedPopTheBalloonEvent() {
        return joinedPopTheBalloonEvent;
    }

    public void setJoinedPopTheBalloonEvent(boolean joinedPopTheBalloonEvent) {
        this.joinedPopTheBalloonEvent = joinedPopTheBalloonEvent;
    }
}
