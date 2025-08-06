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
@Table(name = "notifications")

public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10000)
    private String message;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    private boolean isRead = true;

    @ManyToOne
    @JoinColumn(name = "user_uid")
    private User user;

    @CreationTimestamp
    private LocalDate createdAt;

}
