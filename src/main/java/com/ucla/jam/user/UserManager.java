package com.ucla.jam.user;

import com.ucla.jam.recommendation.RecommendationService;
import com.ucla.jam.util.Distance;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages user creation, profile updating, and preference updating.
 */
@RequiredArgsConstructor
public class UserManager {

    private static final Distance DEFAULT_MAX_DISTANCE = new Distance(50, Distance.Unit.MILES);

    private final UserRepository userRepository;
    private final RecommendationService recommendationService;

    /**
     * Creates a new user with the given profile.
     * Also calls {@link RecommendationService#triggerInsertUser(User)}
     * @param profile New user's profile
     * @return Created user
     */
    public User addNewUser(User.Profile profile) {
        User newUser = new User(UUID.randomUUID(), profile, new User.Preferences(DEFAULT_MAX_DISTANCE, List.of()));
        recommendationService.triggerInsertUser(newUser);
        userRepository.insert(newUser);
        return newUser;
    }

    /**
     * Find user with given id
     * @param userId User UUID
     * @return Empty optional if no user exists with that UUID, optional of user otherwise
     */
    public Optional<User> getUser(UUID userId) {
        return userRepository.find(userId);
    }

    /**
     * Update given user's profile.
     * Also calls {@link RecommendationService#updateUser(User, User.Profile)}
     * @param user Old user
     * @param profile New user profile
     */
    public void updateUserProfile(User user, User.Profile profile) {
        recommendationService.updateUser(user, profile);
        userRepository.insert(user.withProfile(profile));
    }

    /**
     * Update given user's preferences.
     * @param user Old user
     * @param preferences New user profile
     */
    public void updateUserPreferences(User user, User.Preferences preferences) {
        userRepository.insert(user.withPreferences(preferences));
    }
}
