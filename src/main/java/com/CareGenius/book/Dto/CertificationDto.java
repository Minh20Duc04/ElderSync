package com.CareGenius.book.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class CertificationDto {

    private String certificateName;

    private LocalDate issueDate;

    private LocalDate expirationDate;

    private String issuer;

}
