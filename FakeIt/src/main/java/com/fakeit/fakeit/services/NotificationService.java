package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getPendingNotifications(String userId);
    String respondToNotification(String notificationId, String userId, boolean accept);
}
