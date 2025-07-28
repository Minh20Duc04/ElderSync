package com.CareGenius.book.Repository;

import com.CareGenius.book.Model.CareGiverSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareGiverSkillRepository extends JpaRepository<CareGiverSkill, Long> {
}
