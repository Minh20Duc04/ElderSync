package com.CareGenius.book.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tasks")

public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @CreationTimestamp
    private LocalDate createdAt;


}
