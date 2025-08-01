package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.CareGiverSkill;
import com.CareGenius.book.Model.CareNeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CareGiverSkillRepository extends JpaRepository<CareGiverSkill, Long> {
    Set<CareNeed> findAllByCareGiver(CareGiver giverDB);
}
