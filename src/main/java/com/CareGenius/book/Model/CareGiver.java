package com.CareGenius.book.Model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "care_givers")

public class CareGiver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    private LocalDate dob;

    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_uid")
    private User user;

    private Integer yearExperience;

    private String bio;

    private Double fee;

    private String imageUrl;

    @OneToMany(mappedBy = "careGiver", cascade = CascadeType.ALL)
    private Set<CareGiverSkill> skills;

    @OneToMany(mappedBy = "careGiver", cascade = CascadeType.ALL)
    private Set<Certification> certifications;


    @OneToOne(mappedBy = "careGiver", cascade = CascadeType.ALL)
    private Schedule schedule;

}
