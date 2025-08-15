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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //getAll, getById, delete
    @GetMapping("/getAll")
    public ResponseEntity<List<CareSeekerResponseDto>> getAllCareSeeker(){ //ko cần cụ thể, admin nào cũng xem đc
        return ResponseEntity.ok(careSeekerService.getAllCareSeeker());
    }

    @GetMapping("/getById/{seekerUid}")
    public ResponseEntity<CareSeekerResponseDto> getCareSeekerById(Authentication auth, @PathVariable("seekerUid") String seekerUid){ //seeker xem đc profile và giver xem đc seeker, admin
        User userDB =(User) auth.getPrincipal();
        return ResponseEntity.ok(careSeekerService.getCareSeekerById(userDB, seekerUid));
    }

    @DeleteMapping("/deleteById/{seekerUid}") //quyền admin
    public ResponseEntity<String> deleteCareSeekerById(@PathVariable("seekerUid") String seekerUid){
        return ResponseEntity.ok(careSeekerService.deleteCareSeekerById(seekerUid));
    }


}
