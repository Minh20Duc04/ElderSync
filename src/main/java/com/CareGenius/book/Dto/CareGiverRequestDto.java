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

public class CareGiverRequestDto {

    private LocalDate dob;

    private String phoneNumber;

    private Integer yearExperience;

    private Double fee;

    private String bio;

}
