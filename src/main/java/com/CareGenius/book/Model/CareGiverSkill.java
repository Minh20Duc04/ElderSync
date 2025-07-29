package com.CareGenius.book.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "care_giver_skills")

public class CareGiverSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private CareNeed skillName;

    @ManyToOne
    @JoinColumn(name = "care_giver_uid")
    @JsonBackReference
    private CareGiver careGiver;

}
