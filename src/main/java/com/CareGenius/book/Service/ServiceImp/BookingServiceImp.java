package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Repository.BookingRepository;
import com.CareGenius.book.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor


public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;











}
