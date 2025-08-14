package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.ReviewsDto;
import com.CareGenius.book.Model.*;
import com.CareGenius.book.Repository.BookingRepository;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.ReviewsRepository;
import com.CareGenius.book.Service.ReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ReviewsServiceImp implements ReviewsService {

    private final ReviewsRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final CareGiverRepository careGiverRepository;
    

    @Override
    public ReviewsDto createReview(User userDB, ReviewsDto reviewsDto) {
        Booking bookingDB = bookingRepository.findById(reviewsDto.getBookingId()).orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // seeker nào review booking đó, phải xong mới review đc,
        if(!bookingDB.getCareSeeker().getUid().equals(userDB.getCareSeeker().getUid())) {
            throw new RuntimeException("You don't have permission to review this booking !");
        }
        if(!bookingDB.getType().equals(Type.BOOKING_COMPLETED)) {
            throw new RuntimeException("This booking is not done for you to write a review !");
        }

        Reviews createReview = Reviews.builder()
                .comment(reviewsDto.getComment())
                .rating(reviewsDto.getRating())
                .booking(bookingDB)
                .build();

        reviewRepository.save(createReview);
        return mapToReviewsDto(createReview);
    }

    @Override
    public List<ReviewsDto> getReviewsByCareGiver(String careGiverUid) {
        //1 Giver có nhiều Booking, 1 Booking có nhiều review

        CareGiver careGiverDB = careGiverRepository.findById(careGiverUid).orElseThrow(()->new IllegalArgumentException("Not found CareGiver"));

        List<Booking> bookingsByGiver= bookingRepository.findByCareGiver(careGiverDB);

        List<ReviewsDto> reviewsDtos = new LinkedList<>();

        bookingsByGiver.stream().forEach((bookFound)
                -> bookFound.getReviews().forEach((rv)-> reviewsDtos.add(mapToReviewsDto(rv))
                )
        );

        if(reviewsDtos.isEmpty()){
            throw new RuntimeException("Maybe this CareGiver has no reviews");
        }
        return reviewsDtos;
    }

    @Override
    public ReviewsDto updateReview(User userDB, Long reviewId, ReviewsDto reviewDto) {
        Reviews reviewDB = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));

        if(!reviewDB.getBooking().getCareSeeker().getUid().equals(userDB.getCareSeeker().getUid())) {
            throw new RuntimeException("You don't have permission to update this review");
        }

        reviewDB.setComment(reviewDto.getComment());
        reviewDB.setRating(reviewDto.getRating());

        reviewRepository.save(reviewDB);
        return mapToReviewsDto(reviewDB);
    }

    @Override
    public String deleteReview(User userDB, Long reviewId) {
        Reviews reviewDB = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));

        if(!reviewDB.getBooking().getCareSeeker().getUid().equals(userDB.getCareSeeker().getUid())) {
            throw new RuntimeException("You don't have permission to delete this review");
        }

        reviewRepository.delete(reviewDB);
        return "Review deleted successfully";
    }

    private ReviewsDto mapToReviewsDto(Reviews createReview) {
        return new ReviewsDto(
                createReview.getId(),
                createReview.getBooking().getId(),
                createReview.getRating(),
                createReview.getComment()
        );
    }
    
    
    
    
    
}
