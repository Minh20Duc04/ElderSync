package com.CareGenius.book.Model;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonBackReference("certifications-reference")
    private CareGiver careGiver;

    @NotBlank(message = "Certificate name is required")
    private String certificateName;

    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "Issue date must be today or in the past")
    private LocalDate issueDate;

    @NotNull(message = "Expiration date is required")
    private LocalDate expirationDate;

    @NotBlank(message = "Issuer is required")
    private String issuer;

}
