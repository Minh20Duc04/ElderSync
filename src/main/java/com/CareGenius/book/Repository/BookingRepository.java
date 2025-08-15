package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.Booking;
import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.CareSeeker;
import com.CareGenius.book.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    long countByCareGiverAndFromDate(CareGiver careGiverDB, LocalDate fromDate);

    List<Booking> findByCareSeeker(CareSeeker careSeekerDB);

    List<Booking> findByCareGiver(CareGiver careGiverDB);

    boolean existsByCareSeeker_UidAndCareGiver_Uid(String careSeekerUid, String careGiverUid);

    @Query("""
        SELECT DISTINCT b.careGiver.user 
        FROM Booking b 
        WHERE b.careSeeker.uid = :seekerUid
    """)
    List<User> findAllCareGiverUsersBySeekerUid(@Param("seekerUid") String seekerUid);

}
