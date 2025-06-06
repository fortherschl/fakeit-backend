package com.fakeit.fakeit.facades;

import com.fakeit.fakeit.dtos.NotificationDto;
import com.fakeit.fakeit.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationFacade {

    private final NotificationService service;

    public List<NotificationDto> getPending(String userId) {
        return service.getPendingNotifications(userId);
    }

    public String respond(String notificationId, boolean accept) {
        return service.respondToNotification(notificationId, accept);
    }
}
