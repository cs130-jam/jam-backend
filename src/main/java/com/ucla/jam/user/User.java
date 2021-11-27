package com.ucla.jam.user;

import com.fasterxml.jackson.annotation.JsonValue;
import com.ucla.jam.music.MusicInterest;
import com.ucla.jam.util.Distance;
import com.ucla.jam.util.Location;
import com.ucla.jam.util.jooq.JsonConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;

import java.util.List;
import java.util.UUID;

/**
 * User DTO for communication with database
 */
@Value
public class User {
    @NonNull UUID id;
    @With Profile profile;
    @With Preferences preferences;

    @Value
    public static class Profile {
        String firstName;
        String lastName;
        String bio;
        Location location;
        String pfpUrl;
        List<MusicInterest> musicInterests;
        List<Instrument> instruments;

        public static class Converter extends JsonConverter<Profile> {}
    }

    @Value
    public static class Preferences {
        Distance maxDistance;
        List<Instrument> wantedInstruments;

        public static class Converter extends JsonConverter<Preferences> {}
    }

    @RequiredArgsConstructor
    public enum Instrument {
        ELECTRIC_GUITAR("Electric Guitar", 0),
        ACOUSTIC_GUITAR("Acoustic Guitar", 0),
        VOICE("Voice", 0),
        BASS("Bass", 0),
        DRUMS("Drums", 0),
        PIANO("Piano", 1),
        KEYS("Keys", 1);

        @JsonValue
        public final String name;
        public final int usageFrequency; // low values mean used more frequently
    }

    @Value
    public static class UserView { // used to hide some information from client
        UUID id;
        ProfileView profile;

        public static UserView ofUser(User user) {
            return new UserView(user.getId(), ProfileView.ofProfile(user.getProfile()));
        }
    }

    @Value
    public static class ProfileView { // used to hide some information from client
        String firstName;
        String lastName;
        String bio;
        String pfpUrl;
        List<MusicInterest> musicInterests;
        List<Instrument> instruments;

        public static ProfileView ofProfile(Profile profile) {
            return new ProfileView(
                    profile.getFirstName(),
                    profile.getLastName(),
                    profile.getBio(),
                    profile.getPfpUrl(),
                    profile.getMusicInterests(),
                    profile.getInstruments());
        }
    }
}
