package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.Notifications;
import com.CareGenius.book.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    Optional<Notifications> findByUser(User userDB);

    List<Notifications> findByUserOrderByCreatedAtDesc(User userDB);
}
