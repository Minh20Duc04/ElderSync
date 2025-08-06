package com.CareGenius.book.Controller;

import com.CareGenius.book.Dto.NotificationsDto;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor

public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/getAll")
    public ResponseEntity<List<NotificationsDto>> getNotification(Authentication auth){
        User userDB = (User) auth.getPrincipal();
        return ResponseEntity.ok(notificationService.getNotification(userDB));
    }

}
