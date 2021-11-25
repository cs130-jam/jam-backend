package com.ucla.jam.resources;

import com.ucla.jam.chat.ChatManager;
import com.ucla.jam.music.MusicInterest;
import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import com.ucla.jam.user.UnknownUserException;
import com.ucla.jam.user.User;
import com.ucla.jam.user.User.UserView;
import com.ucla.jam.user.UserManager;
import com.ucla.jam.util.Distance;
import com.ucla.jam.util.Location;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
public class UserResource {

    private final UserManager userManager;
    private final ChatManager chatManager;

    @GetMapping(value = "user", produces = APPLICATION_JSON_VALUE)
    public User getSessionUser(@SessionFromHeader SessionInfo sessionInfo) {
        return userManager.getUser(sessionInfo.getUserId())
                .orElseThrow(UnknownUserException::new);
    }

    @PutMapping(value = "user/profile", consumes = APPLICATION_JSON_VALUE)
    public void setSessionUserInfo(@RequestBody UserProfileUpdate update, @SessionFromHeader SessionInfo sessionInfo) {
        User user = userManager.getUser(sessionInfo.getUserId())
                .orElseThrow(UnknownUserException::new);
        userManager.updateUserProfile(user, update.toProfile(user.getProfile()));
    }

    @GetMapping(value = "user/{userId}", produces = APPLICATION_JSON_VALUE)
    public UserView getUser(@PathVariable UUID userId) {
        return UserView.ofUser(userManager.getUser(userId)
                .orElseThrow(UnknownUserException::new));
    }

    @GetMapping(value = "user/{userId}/chatroom", produces = APPLICATION_JSON_VALUE)
    public ChatroomIdResponse getDmChatroom(@PathVariable UUID userId, @SessionFromHeader SessionInfo sessionInfo) {
        return new ChatroomIdResponse(chatManager.ensureDmChatroom(sessionInfo.getUserId(), userId));
    }

    @PutMapping(value = "user/preferences", consumes = APPLICATION_JSON_VALUE)
    public void updatePreferences(@RequestBody UserPreferencesUpdate update, @SessionFromHeader SessionInfo sessionInfo) {
        User user = userManager.getUser(sessionInfo.getUserId())
                .orElseThrow(UnknownUserException::new);
        userManager.updateUserPreferences(user, update.toPreferences(user.getPreferences()));
    }

    @GetMapping(value = "/user/choices/instruments", produces = APPLICATION_JSON_VALUE)
    public List<String> instruments() {
        return Arrays.stream(User.Instrument.values())
                .sorted((t1, t2) -> t2.usageFrequency - t1.usageFrequency) // descending order
                .map(instrument -> instrument.name)
                .collect(toList());
    }

    @GetMapping(value = "/user/choices/maxDistance/units", produces = APPLICATION_JSON_VALUE)
    public List<String> maxDistanceUnits() {
        return Arrays.stream(Distance.Unit.values())
                .map(unit -> unit.name)
                .collect(toList());
    }

    @Value
    private static class UserProfileUpdate {
        String firstName;
        String lastName;
        Location location;
        String bio;
        List<MusicInterest> musicInterests;
        List<User.Instrument> instruments;

        public User.Profile toProfile(User.Profile existingProfile) {
            return new User.Profile(
                    firstName,
                    lastName,
                    bio,
                    location,
                    existingProfile.getPfpUrl(),
                    musicInterests,
                    instruments
            );
        }
    }

    @Value
    private static class UserPreferencesUpdate {
        Distance maxDistance;
        List<User.Instrument> wantedInstruments;

        public User.Preferences toPreferences(User.Preferences existingPreferences) {
            return new User.Preferences(
                    maxDistance,
                    wantedInstruments
            );
        }
    }

    @Value
    private static class ChatroomIdResponse {
        UUID roomId;
    }
}
