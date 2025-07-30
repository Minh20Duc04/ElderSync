package com.CareGenius.book.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class ScheduleDto {
    private Set<DayOfWeek> dayOfWeeks;

    private LocalTime startTime;

    private LocalTime endTime;
}
