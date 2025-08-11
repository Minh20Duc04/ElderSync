package com.CareGenius.book.Dto;

import com.CareGenius.book.Model.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDecision {

    private Long bookingId;

    private Type type;

    private String meetingLink;
}
