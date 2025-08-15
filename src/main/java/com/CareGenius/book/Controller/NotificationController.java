package com.CareGenius.book.Controller;

import com.CareGenius.book.Dto.NotificationsDto;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    //Nút khẩn cấp: vd có 1 form khẩn cấp, khi bấm vào sẽ gửi thông báo đến các giver chăm sóc seeker: Người cần chăm sóc đang cần bạn ngay bây giờ, hãy lập tức đến
    //seeker: Hãy giữ bình tĩnh ! Người chăm sóc đang trên đường đến
    @PostMapping("/emergency")
    public ResponseEntity<?> requestEmergency(Authentication auth){
        User userDB = (User) auth.getPrincipal();
        notificationService.requestEmergency(userDB);
//        return ResponseEntity.ok("Wait for your hero to save you !");

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Wait for your hero to save you and pray !",
                        "timestamp", LocalDateTime.now()
                )
        );
    }


}
