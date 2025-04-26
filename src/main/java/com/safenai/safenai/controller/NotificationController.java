package com.safenai.safenai.controller;

import com.safenai.safenai.model.Notification;
import com.safenai.safenai.dto.NotificationDTO;
import com.safenai.safenai.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    // Get unread notifications for a specific user
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        List<NotificationDTO> notificationDTOs = notifications.stream()
                .map(notification -> {
                    NotificationDTO dto = new NotificationDTO();
                    dto.setId(notification.getId());
                    dto.setMessage(notification.getMessage());
                    dto.setRead(notification.isRead());
                    dto.setCreatedAt(notification.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(notificationDTOs);
    }

    // Mark a notification as read
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                    return ResponseEntity.ok("Notification marked as read");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all notifications for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getAllNotificationsByUser(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findAllByUserId(userId);
        return ResponseEntity.ok(notifications);
    }
}