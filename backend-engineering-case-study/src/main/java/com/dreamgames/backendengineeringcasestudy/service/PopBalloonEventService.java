package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.*;
import com.dreamgames.backendengineeringcasestudy.entity.*;
import com.dreamgames.backendengineeringcasestudy.repository.PopBalloonEventInvitationRepository;
import com.dreamgames.backendengineeringcasestudy.repository.PopBalloonEventParticipationRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PopBalloonEventService {

    private final PopBalloonEventParticipationRepository participationRepository;
    private final PopBalloonEventInvitationRepository invitationRepository;
    private final UserRepository userRepository;

    private static final int COST_TO_JOIN = 2500;
    private static final int LEVEL_REQUIREMENT = 50;
    private static final int GROUP_A_TARGET = 1000;
    private static final int GROUP_B_TARGET = 1500;
    private static final int GROUP_A_REWARD = 1000;
    private static final int GROUP_B_REWARD = 1500;

    private final Random random = new Random();

    public PopBalloonEventService(PopBalloonEventParticipationRepository participationRepository,
                                  PopBalloonEventInvitationRepository invitationRepository,
                                  UserRepository userRepository) {
        this.participationRepository = participationRepository;
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
    }


    public boolean isEventActive() {

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        int hour = nowUtc.getHour();
        return hour >= 8 && hour < 22;
    }

    public LocalDate currentEventDate() {
        return LocalDate.now(ZoneOffset.UTC);
    }

    @Transactional
    public void joinEvent(Integer userId) {
        if (!isEventActive()) {
            throw new IllegalStateException("Event not active");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getLevel() < LEVEL_REQUIREMENT) {
            throw new IllegalArgumentException("User does not meet level requirement to join event");
        }

        if (user.getCoins() < COST_TO_JOIN) {
            throw new IllegalArgumentException("Not enough coins to join event");
        }

        LocalDate eventDate = currentEventDate();
        Optional<PopBalloonEventParticipation> existingParticipation = participationRepository.findByUserIdAndEventDate(userId, eventDate);
        if (existingParticipation.isPresent()) {
            throw new IllegalStateException("User already participating in today's event");
        }

        user.setCoins(user.getCoins() - COST_TO_JOIN);
        userRepository.save(user);

        PopBalloonEventParticipation participation = new PopBalloonEventParticipation();
        participation.setUserId(userId);
        participation.setEventDate(eventDate);
        participationRepository.save(participation);
    }

    public int getBalloonTargetForUser(User user) {
        return user.getAbGroup() == AbGroup.A ? GROUP_A_TARGET : GROUP_B_TARGET;
    }

    public int getRewardForUser(User user) {
        return user.getAbGroup() == AbGroup.A ? GROUP_A_REWARD : GROUP_B_REWARD;
    }

    @Transactional
    public void invitePartner(Integer inviterId, Integer inviteeId) {
        if (!isEventActive()) {
            throw new IllegalStateException("Event not active");
        }

        LocalDate eventDate = currentEventDate();
        PopBalloonEventParticipation inviterParticipation = participationRepository.findByUserIdAndEventDate(inviterId, eventDate)
                .orElseThrow(() -> new IllegalStateException("Inviter not participating in today's event"));

        if (inviterParticipation.isHasPartner()) {
            throw new IllegalStateException("Inviter already has a partner");
        }

        User invitee = userRepository.findById(inviteeId)
                .orElseThrow(() -> new IllegalArgumentException("Invitee user not found"));

        User inviter = userRepository.findById(inviterId).orElseThrow();

        if (!isEventActive()) {
            throw new IllegalStateException("Event not active");
        }

        if (invitee.getAbGroup() != inviter.getAbGroup()) {
            throw new IllegalArgumentException("Invitee not in the same A/B group");
        }

        PopBalloonEventInvitation invitation = new PopBalloonEventInvitation();
        invitation.setInviterUserId(inviterId);
        invitation.setInviteeUserId(inviteeId);
        invitation.setEventDate(eventDate);
        invitationRepository.save(invitation);
    }

    public List<Integer> getSuggestions(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        LocalDate eventDate = currentEventDate();

        //I prepared a new Query for searching, faster, less memory.
        List<Integer> eligibleUserIds = participationRepository.findEligibleSuggestions(eventDate, user.getAbGroup(), userId);

        // Randomly select up to 10
        Collections.shuffle(eligibleUserIds, random);
        return eligibleUserIds.stream().limit(10).collect(Collectors.toList());
    }

    public List<PopBalloonEventInvitation> getPendingInvitations(Integer userId) {
        LocalDate eventDate = currentEventDate();
        return invitationRepository.findByInviteeUserIdAndEventDateAndStatus(userId, eventDate, InvitationStatus.PENDING);
    }

    @Transactional
    public void acceptInvitation(Integer invitationId, Integer userId) {
        LocalDate eventDate = currentEventDate();
        PopBalloonEventInvitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        if (!invitation.getEventDate().equals(eventDate)) {
            throw new IllegalStateException("Invitation is not for today's event");
        }
        if (!invitation.getInviteeUserId().equals(userId)) {
            throw new IllegalArgumentException("This invitation does not belong to you");
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("Invitation already accepted or rejected");
        }

        PopBalloonEventParticipation inviteeParticipation = participationRepository.findByUserIdAndEventDate(userId, eventDate)
                .orElseThrow(() -> new IllegalStateException("Invitee not participating in today's event. Must join event first."));
        PopBalloonEventParticipation inviterParticipation = participationRepository.findByUserIdAndEventDate(invitation.getInviterUserId(), eventDate)
                .orElseThrow(() -> new IllegalStateException("Inviter not participating in today's event"));

        if (inviteeParticipation.isHasPartner() || inviterParticipation.isHasPartner()) {
            throw new IllegalStateException("One of the participants already has a partner");
        }

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);

        // Set partner info
        inviteeParticipation.setHasPartner(true);
        inviteeParticipation.setPartnerUserId(inviterParticipation.getUserId());
        participationRepository.save(inviteeParticipation);

        inviterParticipation.setHasPartner(true);
        inviterParticipation.setPartnerUserId(inviteeParticipation.getUserId());
        participationRepository.save(inviterParticipation);
    }

    @Transactional
    public void rejectInvitation(Integer invitationId, Integer userId) {
        LocalDate eventDate = currentEventDate();
        PopBalloonEventInvitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        if (!invitation.getEventDate().equals(eventDate)) {
            throw new IllegalStateException("Invitation is not for today's event");
        }
        if (!invitation.getInviteeUserId().equals(userId)) {
            throw new IllegalArgumentException("This invitation does not belong to you");
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("Invitation already accepted or rejected");
        }

        invitation.setStatus(InvitationStatus.REJECTED);
        invitationRepository.save(invitation);
    }

    @Transactional
    public UpdateBalloonProgressResponse updateBalloonProgress(Integer userId, int heliumToUse) {
        if (!isEventActive()) {
            throw new IllegalStateException("Event not active");
        }

        LocalDate eventDate = currentEventDate();
        PopBalloonEventParticipation participation = participationRepository.findByUserIdAndEventDate(userId, eventDate)
                .orElseThrow(() -> new IllegalArgumentException("User not participating in today's event"));

        if (!participation.isHasPartner()) {
            throw new IllegalStateException("User has no partner. Cannot inflate balloon alone.");
        }
        if (participation.isHasPopped()) {
            throw new IllegalStateException("Balloon already popped");
        }

        if (participation.getHeliumCollected() < heliumToUse) {
            throw new IllegalArgumentException("Not enough helium collected to inflate the balloon");
        }

        User user = userRepository.findById(userId).orElseThrow();
        int target = getBalloonTargetForUser(user);

        // Inflate balloon
        participation.setHeliumCollected(participation.getHeliumCollected() - heliumToUse);
        participation.setBalloonInflatedAmount(participation.getBalloonInflatedAmount() + heliumToUse);
        if (participation.getBalloonInflatedAmount() >= target) {
            participation.setHasPopped(true);
            PopBalloonEventParticipation partnerParticipation = participationRepository
                    .findByUserIdAndEventDate(participation.getPartnerUserId(), eventDate)
                    .orElseThrow();
            partnerParticipation.setHasPopped(true);
            participationRepository.save(partnerParticipation);
        }

        participationRepository.save(participation);

        UpdateBalloonProgressResponse response = new UpdateBalloonProgressResponse();
        response.setBalloonInflatedAmount(participation.getBalloonInflatedAmount());
        response.setPopped(participation.isHasPopped());
        return response;
    }

    public GetBalloonsInfoResponse getBalloonsInfo(Integer userId) {
        LocalDate eventDate = currentEventDate();
        PopBalloonEventParticipation participation = participationRepository.findByUserIdAndEventDate(userId, eventDate)
                .orElseThrow(() -> new IllegalArgumentException("User not participating in today's event"));

        GetBalloonsInfoResponse response = new GetBalloonsInfoResponse();
        response.setUserId(userId);
        response.setPartnerUserId(participation.getPartnerUserId());
        response.setBalloonInflatedAmount(participation.getBalloonInflatedAmount());
        response.setHeliumCollected(participation.getHeliumCollected());
        response.setHasPopped(participation.isHasPopped());
        response.setRewardClaimed(participation.isRewardClaimed());

        return response;
    }

    @Transactional
    public int claimReward(Integer userId) {
        LocalDate eventDate = currentEventDate();
        PopBalloonEventParticipation participation = participationRepository.findByUserIdAndEventDate(userId, eventDate)
                .orElseThrow(() -> new IllegalArgumentException("User not participating in today's event"));

        if (!participation.isHasPopped()) {
            throw new IllegalStateException("Balloon not popped yet, no reward available");
        }
        if (participation.isRewardClaimed()) {
            throw new IllegalStateException("Reward already claimed");
        }

        User user = userRepository.findById(userId).orElseThrow();
        int reward = getRewardForUser(user);

        user.setCoins(user.getCoins() + reward);
        userRepository.save(user);

        participation.setRewardClaimed(true);
        participationRepository.save(participation);

        return user.getCoins();
    }
}
