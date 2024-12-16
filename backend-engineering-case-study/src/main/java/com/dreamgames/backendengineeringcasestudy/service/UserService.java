// src/main/java/com/dreamgames/backendengineeringcasestudy/service/UserService.java

package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.UpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.dto.UpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.entity.AbGroup;
import com.dreamgames.backendengineeringcasestudy.entity.PopBalloonEventParticipation;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.repository.PopBalloonEventParticipationRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PopBalloonEventParticipationRepository participationRepository;
    private final Random random;

    public UserService(UserRepository userRepository, PopBalloonEventParticipationRepository participationRepository) {
        this.userRepository = userRepository;
        this.participationRepository = participationRepository;
        this.random = new Random();
    }

    public User createUser() {
        // Assign 'A' or 'B' with 50% chance
        AbGroup abGroup = random.nextBoolean() ? AbGroup.A : AbGroup.B;

        User user = new User();
        user.setAbGroup(abGroup); // Corrected

        // level and coins are set by default in the User entity
        return userRepository.save(user);
    }

    @Transactional
    public UpdateLevelResponse updateLevel(UpdateLevelRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + request.getUserId() + " not found"));

        user.setLevel(user.getLevel() + 1);
        user.setCoins(user.getCoins() + 100);

        // Now handle helium via event participation
        int helium = 0;  // default helium to return in the response
        if (request.isJoinedPopTheBalloonEvent()) {
            LocalDate eventDate = LocalDate.now(ZoneOffset.UTC);
            PopBalloonEventParticipation participation = participationRepository
                    .findByUserIdAndEventDate(user.getId(), eventDate)
                    .orElse(null);

            if (participation != null) {
                // User is participating, increment helium by 10
                participation.setHeliumCollected(participation.getHeliumCollected() + 10);
                participationRepository.save(participation);
                helium = participation.getHeliumCollected();
            }
        }

        userRepository.save(user);

        return new UpdateLevelResponse(
                user.getId(),
                user.getLevel(),
                user.getCoins(),
                helium // Helium from participation record if present
        );
    }
}
