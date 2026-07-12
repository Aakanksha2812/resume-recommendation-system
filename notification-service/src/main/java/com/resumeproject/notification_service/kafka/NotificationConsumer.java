package com.resumeproject.notification_service.kafka;

import com.resumeproject.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "resume-parsed", groupId = "notification-group")
    void consumeResumeParsedEvent(String resumeId) {
        log.info("Recivied event from topic resume-parsed for resume id {} ",resumeId);
        notificationService.sendNotification(resumeId);
    }
}
