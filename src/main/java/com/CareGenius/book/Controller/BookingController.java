package com.CareGenius.book.Controller;

import com.CareGenius.book.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor

public class BookingController {

    private final BookingService bookingService;







}
