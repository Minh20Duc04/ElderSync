package com.CareGenius.book.Controller;

import com.CareGenius.book.Dto.BookingDecision;
import com.CareGenius.book.Dto.BookingDto;
import com.CareGenius.book.Model.Type;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PutMapping("/decide")
    public ResponseEntity<String> createBooking (Authentication auth, @RequestBody BookingDecision bookingDecision) throws Exception {
        User userDB = (User) auth.getPrincipal();
        return ResponseEntity.ok(bookingService.decideBooking(userDB, bookingDecision));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BookingDto>> getALlBooking (Authentication auth){
        User userDB = (User) auth.getPrincipal();
        return ResponseEntity.ok(bookingService.getAll(userDB));
    }


}
