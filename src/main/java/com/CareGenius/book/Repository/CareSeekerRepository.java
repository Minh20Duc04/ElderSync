package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.CareSeeker;
import com.CareGenius.book.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareSeekerRepository extends JpaRepository<CareSeeker, String> {

    CareSeeker findByUserUid(String uid);

    CareSeeker findByUser(User userDB);
}
