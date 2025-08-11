package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.BookingDecision;
import com.CareGenius.book.Dto.BookingDto;
import com.CareGenius.book.Model.Type;
import com.CareGenius.book.Model.User;

public interface BookingService {

    BookingDto createBooking(User userDB, BookingDto bookingDto);

    String decideBooking(User userDB, BookingDecision bookingDecision) throws Exception;
}
