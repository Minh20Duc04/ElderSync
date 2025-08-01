package com.CareGenius.book.Controller;

import com.CareGenius.book.Dto.CareSeekerRequestDto;
import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.CareSeeker;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Service.AIRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai-recommend")
@RequiredArgsConstructor

public class AIRecommendationController {

    private final AIRecommendationService aiRecommendationService;

    // Gọi sau khi submit form seeker
    @GetMapping("/match")
    public ResponseEntity<List<CareGiver>> matchGivers(Authentication auth) {
        User userDB = (User) auth.getPrincipal();
        CareSeeker careSeekerDB = userDB.getCareSeeker();

        List<CareGiver> matched = aiRecommendationService.AIRecommendationMatching(careSeekerDB);
        return ResponseEntity.ok(matched);
    }
}
