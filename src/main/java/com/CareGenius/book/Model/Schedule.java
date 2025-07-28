package com.CareGenius.book.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "schedule")

public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "care_giver_uid")
    private CareGiver careGiver;

    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(name = "schedule_days", joinColumns = @JoinColumn(name = "schedule_id"))
    @Enumerated(value = EnumType.STRING)
    private Set<DayOfWeek> dayOfWeeks;

    private LocalTime startTime;

    private LocalTime endTime;

}
