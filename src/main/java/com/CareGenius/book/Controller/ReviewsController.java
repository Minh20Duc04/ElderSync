package com.CareGenius.book.Controller;

import com.CareGenius.book.Dto.ReviewsDto;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Service.ReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor

public class ReviewsController {

    private final ReviewsService reviewsService;

    @PostMapping("/create")
    public ResponseEntity<ReviewsDto> createReview(Authentication auth, @RequestBody ReviewsDto reviewsDto) {
        User userDB = (User) auth.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewsService.createReview(userDB, reviewsDto));
    }

    @GetMapping("/getByGiverId/{careGiverUid}")
    public ResponseEntity<List<ReviewsDto>> getReviewsByCareGiver(@PathVariable("careGiverUid") String careGiverUid) {
        return ResponseEntity.ok(reviewsService.getReviewsByCareGiver(careGiverUid));
    }

    @PutMapping("/updateByReviewId/{reviewId}")
    public ResponseEntity<ReviewsDto> updateReview(Authentication auth, @PathVariable Long reviewId, @RequestBody ReviewsDto reviewDto) {
        User userDB = (User) auth.getPrincipal();
        return ResponseEntity.ok(reviewsService.updateReview(userDB, reviewId, reviewDto));
    }

    @DeleteMapping("/deleteById/{reviewId}")
    public ResponseEntity<String> deleteReview(Authentication auth, @PathVariable Long reviewId) {
        User userDB = (User) auth.getPrincipal();
        return ResponseEntity.ok(reviewsService.deleteReview(userDB, reviewId));
    }
}
