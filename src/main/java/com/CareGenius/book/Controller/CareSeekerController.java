package com.CareGenius.book.Controller;

import com.CareGenius.book.Dto.CareSeekerRequestDto;
import com.CareGenius.book.Dto.CareSeekerResponseDto;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Repository.CareSeekerRepository;
import com.CareGenius.book.Service.CareSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/care-seekers")
@RequiredArgsConstructor

public class CareSeekerController {

    private final CareSeekerService careSeekerService;

    @PostMapping("/create")
    public ResponseEntity<CareSeekerResponseDto> createCareSeeker(Authentication auth, @RequestBody CareSeekerRequestDto careSeekerRequestDto){

        User userDB =(User) auth.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(careSeekerService.createCareSeeker(userDB, careSeekerRequestDto));
    }

}
