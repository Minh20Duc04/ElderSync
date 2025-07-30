package com.CareGenius.book.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class CareGiverResponseDto {

    private String uid;

    private String fullName;

    private String gender;

    private String imageUrl;

    private int yearExperience;

    private double fee;

    private String bio;

    private Set<String> skills;

    private Set<String> certifications;

    private ScheduleDto schedules;
}
