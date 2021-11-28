package com.ucla.jam.friends;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class InMemoryFriendRequestRepository implements FriendRequestRepository {

    private final List<Request> requests = new ArrayList<>();

    @Override
    public void request(UUID sourceUser, UUID targetUser) {
        if (isRequested(sourceUser, targetUser)) return;
        requests.add(new Request(sourceUser, targetUser));
    }

    @Override
    public void unrequest(UUID sourceUser, UUID targetUser) {
        if (!isRequested(sourceUser, targetUser)) return;
        Request req = new Request(sourceUser, targetUser);
        requests.removeIf(req::equals);
    }

    @Override
    public boolean isRequested(UUID sourceUser, UUID targetUser) {
        Request req = new Request(sourceUser, targetUser);
        return requests.stream()
                .anyMatch(req::equals);
    }

    @Override
    public List<UUID> requestsTo(UUID targetUser) {
        return requests.stream()
                .filter(req -> req.getTarget().equals(targetUser))
                .map(Request::getSource)
                .collect(toList());
    }

    @Override
    public List<UUID> requestsFrom(UUID sourceUser) {
        return requests.stream()
                .filter(req -> req.getSource().equals(sourceUser))
                .map(Request::getTarget)
                .collect(toList());
    }

    @Value
    private static class Request {
        UUID source;
        UUID target;
    }
}
