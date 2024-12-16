package com.dreamgames.backendengineeringcasestudy.dto;

import java.util.List;

public class GetSuggestionsAndInvitationsResponse {
    private List<Integer> suggestedUserIds;
    private List<InvitationDto> pendingInvitations;

    public static class InvitationDto {
        private Integer invitationId;
        private Integer inviterUserId;

        // getters and setters
        public Integer getInvitationId() { return invitationId; }
        public Integer getInviterUserId() { return inviterUserId; }
        public void setInvitationId(Integer invitationId) { this.invitationId = invitationId; }
        public void setInviterUserId(Integer inviterUserId) { this.inviterUserId = inviterUserId; }
    }

    // getters and setters
    public List<Integer> getSuggestedUserIds() {
        return suggestedUserIds;
    }
    public List<InvitationDto> getPendingInvitations() {
        return pendingInvitations;
    }
    public void setSuggestedUserIds(List<Integer> suggestedUserIds) {
        this.suggestedUserIds = suggestedUserIds;
    }
    public void setPendingInvitations(List<InvitationDto> pendingInvitations) {
        this.pendingInvitations = pendingInvitations;
    }
}
