package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.Booking;
import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.CareSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    long countByCareGiverAndFromDate(CareGiver careGiverDB, LocalDate fromDate);

    List<Booking> findByCareSeeker(CareSeeker careSeekerDB);

    List<Booking> findByCareGiver(CareGiver careGiverDB);
}
