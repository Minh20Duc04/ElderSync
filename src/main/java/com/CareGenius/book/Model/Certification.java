package com.CareGenius.book.Model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "care_giver_certification")

public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "care_giver_uid")
    @JsonBackReference
    private CareGiver careGiver;

    private String certificateName;

    private LocalDate issueDate;

    private LocalDate expirationDate;

    private String issuer;

}
