package com.ucla.jam.user;

import com.ucla.jam.recommendation.RecommendationService;
import com.ucla.jam.util.Distance;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UserManager {

    private static final Distance DEFAULT_MAX_DISTANCE = new Distance(50, Distance.Unit.MILES);

    private final UserRepository userRepository;
    private final RecommendationService recommendationService;

    public User addNewUser(User.Profile profile) {
        User newUser = new User(UUID.randomUUID(), profile, new User.Preferences(DEFAULT_MAX_DISTANCE, List.of()));
        recommendationService.triggerInsertUser(newUser);
        userRepository.insert(newUser);
        return newUser;
    }

    public Optional<User> getUser(UUID userId) {
        return userRepository.find(userId);
    }

    public void updateUserProfile(User user, User.Profile profile) {
        recommendationService.updateUser(user, profile);
        userRepository.insert(user.withProfile(profile));
    }
}
