// src/main/java/com/dreamgames/backendengineeringcasestudy/dto/InvitePartnerRequest.java
package com.dreamgames.backendengineeringcasestudy.dto;

public class InvitePartnerRequest {
    private Integer inviterUserId;
    private Integer inviteeUserId;

    public Integer getInviterUserId() {
        return inviterUserId;
    }

    public void setInviterUserId(Integer inviterUserId) {
        this.inviterUserId = inviterUserId;
    }

    public Integer getInviteeUserId() {
        return inviteeUserId;
    }

    public void setInviteeUserId(Integer inviteeUserId) {
        this.inviteeUserId = inviteeUserId;
    }
}
