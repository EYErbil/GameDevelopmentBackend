package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.UpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.dto.UpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.entity.AbGroup;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Random random;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        if (request.isJoinedPopTheBalloonEvent()) {
            user.addHelium(10);
        }

        userRepository.save(user);

        return new UpdateLevelResponse(
                user.getId(),
                user.getLevel(),
                user.getCoins(),
                user.getHelium()
        );
    }
}
