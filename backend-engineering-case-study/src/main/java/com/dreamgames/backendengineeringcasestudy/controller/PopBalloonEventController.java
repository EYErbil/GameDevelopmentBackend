package com.dreamgames.backendengineeringcasestudy.controller;
import com.dreamgames.backendengineeringcasestudy.repository.*;

import com.dreamgames.backendengineeringcasestudy.dto.*;
import com.dreamgames.backendengineeringcasestudy.entity.*;
import com.dreamgames.backendengineeringcasestudy.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pop-balloon-event")
public class PopBalloonEventController {

    private final PopBalloonEventService eventService;

    public PopBalloonEventController(PopBalloonEventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/join")
    public void joinEvent(@RequestParam Integer userId) {
        eventService.joinEvent(userId);
    }

    @PostMapping("/invite")
    public void invitePartner(@RequestBody InvitePartnerRequest request) {
        eventService.invitePartner(request.getInviterUserId(), request.getInviteeUserId());
    }

    @GetMapping("/suggestions-and-invitations")
    public GetSuggestionsAndInvitationsResponse getSuggestionsAndInvitations(@RequestParam Integer userId) {
        List<Integer> suggestions = eventService.getSuggestions(userId);
        List<PopBalloonEventInvitation> invitations = eventService.getPendingInvitations(userId);

        GetSuggestionsAndInvitationsResponse response = new GetSuggestionsAndInvitationsResponse();
        response.setSuggestedUserIds(suggestions);
        List<GetSuggestionsAndInvitationsResponse.InvitationDto> invitationDtos = invitations.stream().map(inv -> {
            GetSuggestionsAndInvitationsResponse.InvitationDto dto = new GetSuggestionsAndInvitationsResponse.InvitationDto();
            dto.setInvitationId(inv.getId());
            dto.setInviterUserId(inv.getInviterUserId());
            return dto;
        }).collect(Collectors.toList());
        response.setPendingInvitations(invitationDtos);
        return response;
    }

    @PostMapping("/accept")
    public void acceptInvitation(@RequestBody AcceptPlayerRequest request) {
        eventService.acceptInvitation(request.getInvitationId(), request.getUserId());
    }

    @PostMapping("/reject")
    public void rejectInvitation(@RequestBody RejectPlayerRequest request) {
        eventService.rejectInvitation(request.getInvitationId(), request.getUserId());
    }

    @PostMapping("/updateBalloon")
    public UpdateBalloonProgressResponse updateBalloon(@RequestBody UpdateBalloonProgressRequest request) {
        return eventService.updateBalloonProgress(request.getUserId(), request.getHeliumToUse());
    }

    @GetMapping("/balloonsInfo")
    public GetBalloonsInfoResponse getBalloonsInfo(@RequestParam Integer userId) {
        return eventService.getBalloonsInfo(userId);
    }

    @PostMapping("/claimReward")
    public ClaimRewardResponse claimReward(@RequestBody ClaimRewardRequest request) {
        int newBalance = eventService.claimReward(request.getUserId());
        ClaimRewardResponse response = new ClaimRewardResponse();
        response.setNewCoinsBalance(newBalance);
        return response;
    }
}
