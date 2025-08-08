package com.CareGenius.book.Controller;

import com.CareGenius.book.Dto.BookingDto;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor

public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<BookingDto> createBooking (Authentication auth, @RequestBody BookingDto bookingDto){
        User userDB = (User) auth.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(userDB, bookingDto));
    }

    //update de confrim, getAll




}
