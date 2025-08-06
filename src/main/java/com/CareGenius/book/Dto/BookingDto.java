package com.CareGenius.book.Dto;

import com.CareGenius.book.Model.Carelocation;
import com.CareGenius.book.Model.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class BookingDto {

    private Carelocation carelocation;

    private LocalDate fromDate;

    private Integer duration;

    private LocalTime startTime;

    private LocalTime endTime;

    private String note;

    private Payment payment;

    private String careGiverUid;

}
