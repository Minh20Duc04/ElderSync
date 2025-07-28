package com.CareGenius.book.Controller;


import com.CareGenius.book.Dto.CareGiverDto;
import com.CareGenius.book.Dto.CareGiverRequestDto;
import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Service.CareGiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/caregivers")
@RequiredArgsConstructor

public class CareGiverController {

    private final CareGiverService careGiverService;

    @PostMapping("/create")
    public ResponseEntity<CareGiver> createCareGiver(@RequestBody CareGiverDto careGiverDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(careGiverService.createCareGiver(careGiverDto));
    }

}
