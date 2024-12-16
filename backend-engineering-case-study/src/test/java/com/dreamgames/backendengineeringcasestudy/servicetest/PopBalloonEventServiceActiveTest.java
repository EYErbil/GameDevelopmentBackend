package com.dreamgames.backendengineeringcasestudy.servicetest;
import static org.mockito.Mockito.lenient;

import com.dreamgames.backendengineeringcasestudy.entity.*;
import com.dreamgames.backendengineeringcasestudy.repository.*;
import com.dreamgames.backendengineeringcasestudy.service.PopBalloonEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test") // Ensure this profile sets forceEventActive=true
public class PopBalloonEventServiceActiveTest {

    @Mock
    private PopBalloonEventParticipationRepository participationRepository;

    @Mock
    private PopBalloonEventInvitationRepository invitationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    @Spy // Add @Spy annotation to enable spying
    private PopBalloonEventService eventService;

    private User userA;
    private User userB;
    private LocalDate eventDate;

    @BeforeEach
    void setUp() {
        // Force isEventActive() to return true
        lenient().doReturn(true).when(eventService).isEventActive();

        userA = new User();
        userA.setId(1);
        userA.setLevel(50);
        userA.setCoins(3000);
        userA.setAbGroup(AbGroup.A);

        userB = new User();
        userB.setId(2);
        userB.setLevel(50);
        userB.setCoins(5000);
        userB.setAbGroup(AbGroup.B);

        eventDate = LocalDate.now(ZoneOffset.UTC);
    }

    @Test
    void testJoinEvent_AlwaysActive() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(userA));
        when(participationRepository.findByUserIdAndEventDate(1, eventDate)).thenReturn(Optional.empty());

        // Act
        eventService.joinEvent(1);

        // Assert
        assertEquals(500, userA.getCoins()); // after paying 2500
        verify(userRepository).save(userA);
        verify(participationRepository).save(any(PopBalloonEventParticipation.class));
    }

    @Test
    void testJoinEvent_LevelTooLow_AlwaysActive() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(userA));
        userA.setLevel(10); // fails level check

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> eventService.joinEvent(1));
        assertEquals("User does not meet level requirement to join event", ex.getMessage());
    }

    @Test
    void testJoinEvent_NotEnoughCoins_AlwaysActive() {
        // Arrange
        userA.setCoins(2000); // not enough
        when(userRepository.findById(1)).thenReturn(Optional.of(userA));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> eventService.joinEvent(1));
        assertEquals("Not enough coins to join event", ex.getMessage());
    }

    @Test
    void testInvitePartner_Success_AlwaysActive() {
        // Arrange
        User invitee = new User();
        invitee.setId(3);
        invitee.setAbGroup(AbGroup.A);
        invitee.setLevel(50);
        invitee.setCoins(3000);

        when(userRepository.findById(1)).thenReturn(Optional.of(userA));
        when(userRepository.findById(3)).thenReturn(Optional.of(invitee));

        PopBalloonEventParticipation inviterPart = new PopBalloonEventParticipation();
        inviterPart.setUserId(1);
        inviterPart.setEventDate(eventDate);
        when(participationRepository.findByUserIdAndEventDate(1, eventDate)).thenReturn(Optional.of(inviterPart));

        // Act
        eventService.invitePartner(1, 3);

        // Assert
        verify(invitationRepository).save(any(PopBalloonEventInvitation.class));
    }

    @Test
    void testInvitePartner_DifferentABGroups_AlwaysActive() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(userA));
        when(userRepository.findById(2)).thenReturn(Optional.of(userB)); // different AB group

        PopBalloonEventParticipation inviterPart = new PopBalloonEventParticipation();
        inviterPart.setUserId(1);
        inviterPart.setEventDate(eventDate);
        when(participationRepository.findByUserIdAndEventDate(1, eventDate)).thenReturn(Optional.of(inviterPart));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> eventService.invitePartner(1, 2));
        assertEquals("Invitee not in the same A/B group", ex.getMessage());
    }

    @Test
    void testUpdateBalloonProgress_Popped_AlwaysActive() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(userA));
        PopBalloonEventParticipation part = new PopBalloonEventParticipation();
        part.setUserId(1);
        part.setEventDate(eventDate);
        part.setHeliumCollected(2000);
        part.setHasPartner(true);
        part.setPartnerUserId(2);
        when(participationRepository.findByUserIdAndEventDate(1, eventDate)).thenReturn(Optional.of(part));

        PopBalloonEventParticipation partnerPart = new PopBalloonEventParticipation();
        partnerPart.setUserId(2);
        partnerPart.setEventDate(eventDate);
        partnerPart.setHasPartner(true);
        when(participationRepository.findByUserIdAndEventDate(2, eventDate)).thenReturn(Optional.of(partnerPart));

        // Act
        var response = eventService.updateBalloonProgress(1, 1200);

        // Assert
        assertTrue(response.isPopped());
    }

    @Test
    void testClaimReward_BalloonNotPopped_AlwaysActive() {
        // Arrange
        PopBalloonEventParticipation part = new PopBalloonEventParticipation();
        part.setUserId(1);
        part.setEventDate(eventDate);
        part.setHasPopped(false);
        when(participationRepository.findByUserIdAndEventDate(1, eventDate)).thenReturn(Optional.of(part));

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> eventService.claimReward(1));
        assertEquals("Balloon not popped yet, no reward available", ex.getMessage());
    }
}
