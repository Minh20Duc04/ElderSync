package com.CareGenius.book.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "care_seekers")

public class CareSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    private LocalDate dob;

    private String phoneNumber;

    @ElementCollection(targetClass = CareNeed.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "care_needs_description", joinColumns = @JoinColumn(name = "care_seeker_uid"))
    @Enumerated(value = EnumType.STRING)
    private Set<CareNeed> careNeedsDescription;

    @ElementCollection(targetClass = HealthCondition.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "health_conditions", joinColumns = @JoinColumn(name = "care_seeker_uid"))
    @Enumerated(value = EnumType.STRING)
    private Set<HealthCondition> healthConditions;

    @Enumerated(value = EnumType.STRING)
    private Gender preferredGiverGender;

    @OneToOne
    @JoinColumn(name = "user_uid")
    private User user;

    @OneToMany(mappedBy = "careSeeker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AIRecommendation> recommendations;

}
