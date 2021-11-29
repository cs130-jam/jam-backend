package com.ucla.jam.user;

import com.ucla.jam.recommendation.RecommendationService;
import com.ucla.jam.util.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserManagerUnitTest {

    private final UserRepository userRepository = new InMemoryUserRepository();

    @Test
    void createNewUser() {
        RecommendationService recommendationService = Mockito.mock(RecommendationService.class);
        UserManager userManager = new UserManager(userRepository, recommendationService);

        User user = userManager.addNewUser(emptyUserProfile("name"));
        assertThat(userRepository.find(user.getId()))
                .isPresent();
    }

    @Test
    void updateUserProfile() {
        RecommendationService recommendationService = Mockito.mock(RecommendationService.class);
        UserManager userManager = new UserManager(userRepository, recommendationService);

        User user = userManager.addNewUser(emptyUserProfile("name"));
        userManager.updateUserProfile(user, emptyUserProfile("new_name"));
        assertThat(userRepository.find(user.getId()))
                .isPresent();
        assertThat(userRepository.find(user.getId()).get().getProfile().getFirstName())
                .isEqualTo("new_name");
    }

    private User.Profile emptyUserProfile(String name) {
        return new User.Profile(name, "", "", new Location("", ""), "", List.of(), List.of());
    }

}
