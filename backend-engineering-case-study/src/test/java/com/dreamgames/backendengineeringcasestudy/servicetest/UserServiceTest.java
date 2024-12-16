// src/test/java/com/dreamgames/backendengineeringcasestudy/servicetest/UserServiceTest.java

package com.dreamgames.backendengineeringcasestudy.servicetest;

import com.dreamgames.backendengineeringcasestudy.dto.UpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.dto.UpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.entity.AbGroup;
import com.dreamgames.backendengineeringcasestudy.entity.PopBalloonEventParticipation;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.repository.PopBalloonEventParticipationRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PopBalloonEventParticipationRepository participationRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);
        user.setAbGroup(AbGroup.A);
        user.setLevel(1);
        user.setCoins(2000);
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User created = userService.createUser();
        assertNotNull(created);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateLevel_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        UpdateLevelRequest req = new UpdateLevelRequest(1, false);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> userService.updateLevel(req));
        assertEquals("User with ID 1 not found", ex.getMessage());
    }

    @Test
    void testUpdateLevel_NoEventParticipation() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        // No participation record
        when(participationRepository.findByUserIdAndEventDate(eq(1), any(LocalDate.class))).thenReturn(Optional.empty());

        UpdateLevelRequest req = new UpdateLevelRequest(1, true);
        UpdateLevelResponse res = userService.updateLevel(req);

        assertEquals(2, res.getLevel());
        assertEquals(2100, res.getCoins());
        assertEquals(0, res.getHelium()); // No participation means no helium increment
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateLevel_WithEventParticipation() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        PopBalloonEventParticipation participation = new PopBalloonEventParticipation();
        participation.setUserId(1);
        participation.setHeliumCollected(0);

        when(participationRepository.findByUserIdAndEventDate(eq(1), any(LocalDate.class)))
                .thenReturn(Optional.of(participation));

        UpdateLevelRequest req = new UpdateLevelRequest(1, true);
        UpdateLevelResponse res = userService.updateLevel(req);

        assertEquals(2, res.getLevel());
        assertEquals(2100, res.getCoins());
        assertEquals(10, res.getHelium()); // Helium incremented by 10
        verify(participationRepository).save(participation);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateLevel_MultipleUpdates() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // First update without event
        UpdateLevelRequest req1 = new UpdateLevelRequest(1, false);
        userService.updateLevel(req1); // level=2, coins=2100

        // Second update with event participation
        PopBalloonEventParticipation part = new PopBalloonEventParticipation();
        part.setUserId(1);
        part.setHeliumCollected(20);
        when(participationRepository.findByUserIdAndEventDate(eq(1), any(LocalDate.class)))
                .thenReturn(Optional.of(part));

        UpdateLevelRequest req2 = new UpdateLevelRequest(1, true);
        UpdateLevelResponse res2 = userService.updateLevel(req2);
        // level=3, coins=2200, helium=30

        assertEquals(3, res2.getLevel());
        assertEquals(2200, res2.getCoins());
        assertEquals(30, res2.getHelium());
    }
}
