package com.ucla.jam.user;

import com.ucla.jam.util.Distance;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UserManager {

    private static final Distance DEFAULT_MAX_DISTANCE = new Distance(50, Distance.Unit.MILES);

    private final UserRepository userRepository;

    public User addNewUser(User.Profile profile) {
        User newUser = new User(UUID.randomUUID(), profile, new User.Preferences(DEFAULT_MAX_DISTANCE, List.of()));
        userRepository.insert(newUser);
        return newUser;
    }

}
