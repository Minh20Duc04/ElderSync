package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.CareGiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareGiverRepository extends JpaRepository<CareGiver, String> {

    boolean existsByPhoneNumber(String phoneNumber);
}
