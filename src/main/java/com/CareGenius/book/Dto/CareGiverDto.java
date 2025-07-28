package com.CareGenius.book.Dto;

import com.CareGenius.book.Model.CareNeed;
import com.CareGenius.book.Model.Certification;
import com.CareGenius.book.Model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class CareGiverDto {

    private UserDto userDto;

    private CareGiverRequestDto careGiverRequestDto;

    private List<CareNeed> skills;

    private List<CertificationDto> certifications;

    private Schedule schedule;

}
