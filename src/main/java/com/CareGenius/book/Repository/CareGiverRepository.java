package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareGiverRepository extends JpaRepository<CareGiver, String> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByUserUid(String uid);

    CareGiver findByUser(User userDB);

    Page<CareGiver> findByUser_FullNameContainingIgnoreCase(String name, Pageable pageable);

}
