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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_role")
    private Role role;

    private LocalDate dob;

    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_uid")
    private User user;

    private Integer yearExperience;

    private String bio;

    private Double fee;

    @OneToMany(mappedBy = "careGiver", cascade = CascadeType.ALL)
    private Set<CareGiverSkill> skills;

    @OneToMany(mappedBy = "careGiver", cascade = CascadeType.ALL)
    private Set<Certification> certifications;

    private String imageUrl;

    private Set<Schedule> daysOfWeek;

}
