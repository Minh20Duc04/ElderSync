package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.Booking;
import com.CareGenius.book.Model.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    Optional<Reviews> findByBooking(Booking booking);
}
