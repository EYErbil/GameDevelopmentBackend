package com.dreamgames.backendengineeringcasestudy.dto;

public class RejectPlayerRequest {
    private Integer invitationId;
    private Integer userId; // The invitee who is rejecting

    public Integer getInvitationId() { return invitationId; }
    public Integer getUserId() { return userId; }
    public void setInvitationId(Integer invitationId) { this.invitationId = invitationId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
