package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.Booking;
import com.CareGenius.book.Model.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {

    List<Tasks> findByBooking(Booking bookingDB);
}
