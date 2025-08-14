package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.ReviewsDto;
import com.CareGenius.book.Model.User;

import java.util.List;

public interface ReviewsService {

    ReviewsDto createReview(User userDB, ReviewsDto reviewsDto);

    List<ReviewsDto> getReviewsByCareGiver(String careGiverUid);

    ReviewsDto updateReview(User userDB, Long reviewId, ReviewsDto reviewDto);

    String deleteReview(User userDB, Long reviewId);

}
