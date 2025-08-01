package com.CareGenius.book.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ai_recommendation")

public class AIRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int matchPoint;

    @ManyToOne
    @JoinColumn(name = "care_seeker_uid")
    private CareSeeker careSeeker;

    @ManyToOne
    @JoinColumn(name = "care_giver_uid")
    private CareGiver careGiver;

    @CreationTimestamp
    private LocalDate createdAt;
}
