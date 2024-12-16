//This might look unnecessary, but I coded this 10 PM canada time, so it was non-active
//event hours, I needed to check if I could code correct the active event side, so I seperated the tests
//by forced - active to make sure they work fine, and here is the non - active one. I didnt want to



package com.dreamgames.backendengineeringcasestudy.servicetest;

import com.dreamgames.backendengineeringcasestudy.entity.*;
import com.dreamgames.backendengineeringcasestudy.repository.*;
import com.dreamgames.backendengineeringcasestudy.service.PopBalloonEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test") // Loads application-inactiveTest.properties => forceEventActive=false
public class PopBalloonEventServiceInactiveTest {

    @Mock
    private PopBalloonEventParticipationRepository participationRepository;
    @Mock
    private PopBalloonEventInvitationRepository invitationRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PopBalloonEventService eventService;

    private LocalDate eventDate;

    @BeforeEach
    void setUp() {
        eventDate = LocalDate.now(ZoneOffset.UTC);
    }

    @Test
    void testJoinEvent_NotActive() {
        // We want a guaranteed inactive scenario:
        PopBalloonEventService spiedService = spy(eventService);
        doReturn(false).when(spiedService).isEventActive();

        // Since event is not active, code throws immediately, no user/participation stubs needed
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> spiedService.joinEvent(1));
        assertEquals("Event not active", ex.getMessage());
    }

    @Test
    void testUpdateBalloonProgress_NotActive() {
        PopBalloonEventService spiedService = spy(eventService);
        doReturn(false).when(spiedService).isEventActive();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> spiedService.updateBalloonProgress(1, 100));
        assertEquals("Event not active", ex.getMessage());
    }



}
