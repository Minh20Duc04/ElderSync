package com.CareGenius.book.Dto;

import com.CareGenius.book.Model.CareNeed;
import com.CareGenius.book.Model.Gender;
import com.CareGenius.book.Model.HealthCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class CareSeekerResponseDto {

    private String careSeekerId;

    private String fullName;

    private String email;

    private LocalDate dob;

    private String phoneNumber;

    private Set<CareNeed> careNeedsDescription;

    private Set<HealthCondition> healthConditions;

    private Gender preferredGiverGender;
}
