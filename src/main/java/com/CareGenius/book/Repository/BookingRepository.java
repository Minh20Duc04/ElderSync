package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.Booking;
import com.CareGenius.book.Model.CareGiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    long countByCareGiverAndFromDate(CareGiver careGiverDB, LocalDate fromDate);
}
