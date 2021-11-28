package com.ucla.jam.friends;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class InMemoryFriendRepository implements FriendRepository {

    private final List<FriendRelationship> friends = new ArrayList<>();

    @Override
    public List<UUID> getAll(UUID user) {
        return friends.stream()
                .filter(rel -> rel.getUserA().equals(user) || rel.getUserB().equals(user))
                .map(rel -> rel.getUserA().equals(user) ? rel.getUserB() : rel.getUserA())
                .collect(toList());
    }

    @Override
    public void friend(UUID userA, UUID userB) {
        if (getAll(userA).contains(userB)) return;
        friends.add(new FriendRelationship(userA, userB));
    }

    @Override
    public void unfriend(UUID userA, UUID userB) {
        friends.removeIf(rel -> rel.getUserA().equals(userA) && rel.getUserB().equals(userB));
        friends.removeIf(rel -> rel.getUserA().equals(userB) && rel.getUserB().equals(userA));
    }

    @Value
    private static class FriendRelationship {
        UUID userA;
        UUID userB;
    }
}
