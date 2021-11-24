package com.ucla.jam.resources;

import com.ucla.jam.friends.AlreadyFriendsException;
import com.ucla.jam.friends.FriendManager;
import com.ucla.jam.friends.FriendManager.FriendResult;
import com.ucla.jam.friends.FriendManager.UnfriendResult;
import com.ucla.jam.friends.NoRelationshipException;
import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
public class FriendResource {

    private final FriendManager.FriendManagerFactory friendManagerFactory;

    @GetMapping(value = "friends", produces = APPLICATION_JSON_VALUE)
    public List<UUID> getFriends(@SessionFromHeader SessionInfo sessionInfo) {
        return friendManagerFactory.forUser(sessionInfo.getUserId()).getFriends();
    }

    @PutMapping(value = "friends/{targetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void friend(@PathVariable UUID targetId, @SessionFromHeader SessionInfo sessionInfo) {
        FriendResult result = friendManagerFactory.forUser(sessionInfo.getUserId()).tryFriend(targetId);
        if (result == FriendResult.ALREADY_FRIENDS) {
            throw new AlreadyFriendsException();
        }
    }

    @GetMapping(value = "friends/requests", produces = APPLICATION_JSON_VALUE)
    public Requests getRequests(@SessionFromHeader SessionInfo sessionInfo) {
        FriendManager manager = friendManagerFactory.forUser(sessionInfo.getUserId());
        return new Requests(
                manager.getOutgoingRequests(),
                manager.getIncomingRequests()
        );
    }

    @DeleteMapping(value = "friends/{targetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfriend(@PathVariable UUID targetId, @SessionFromHeader SessionInfo sessionInfo) {
        UnfriendResult unfriendResult = friendManagerFactory.forUser(sessionInfo.getUserId()).tryUnfriend(targetId);
        if (unfriendResult == UnfriendResult.NOT_FRIENDS_OR_REQUESTED) {
            throw new NoRelationshipException();
        }
    }

    @PostMapping(value = "friends/requests/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@RequestParam UUID sourceId, @SessionFromHeader SessionInfo sessionInfo) {
        friendManagerFactory.forUser(sourceId).removeRequest(sessionInfo.getUserId());
    }

    @Value
    public static class Requests {
        List<UUID> outgoing;
        List<UUID> incoming;
    }
}
