package com.resumeproject.notification_service.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
  public   void sendNotification(String resumeId) {
        System.out.println( "notification sent for resumeId: " + resumeId);
    }
}
