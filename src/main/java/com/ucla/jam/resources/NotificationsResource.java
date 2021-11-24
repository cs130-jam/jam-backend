package com.ucla.jam.resources;

import com.ucla.jam.notifications.Notification;
import com.ucla.jam.notifications.NotificationManager;
import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
public class NotificationsResource {

    private final NotificationManager notificationManager;

    @GetMapping(value = "/notifications", produces = APPLICATION_JSON_VALUE)
    public List<NotificationEntry> getAll(@SessionFromHeader SessionInfo sessionInfo) {
        return notificationManager.getAll(sessionInfo.getUserId()).stream()
                .map(NotificationEntry::fromNotification)
                .collect(toList());
    }

    @PostMapping(value = "/notifications/{id}/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeNotification(@PathVariable UUID id, @SessionFromHeader SessionInfo sessionInfo) {
        notificationManager.removeNotification(id, sessionInfo.getUserId());
    }

    @Value
    private static class NotificationEntry {
        String title;
        Notification.Action accept;
        Notification.Action reject;

        public static NotificationEntry fromNotification(Notification notification) {
            return new NotificationEntry(
                    notification.getTitle(),
                    notification.getAccept(),
                    notification.getReject());
        }
    }
}
