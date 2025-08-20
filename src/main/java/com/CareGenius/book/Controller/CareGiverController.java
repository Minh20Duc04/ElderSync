package com.CareGenius.book.Controller;


import com.CareGenius.book.Dto.CareGiverDto;
import com.CareGenius.book.Dto.CareGiverResponseDto;
import com.CareGenius.book.Service.CareGiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/caregivers")
@RequiredArgsConstructor

public class CareGiverController {

    private final CareGiverService careGiverService;

    @PostMapping("/create")
    public ResponseEntity<CareGiverDto> createCareGiver(@RequestBody CareGiverDto careGiverDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(careGiverService.createCareGiver(careGiverDto));
    }

    @PostMapping("/linkImage")
    public ResponseEntity<String> linkImageToGiver(@RequestParam String giverUid, @RequestParam MultipartFile file){
        return ResponseEntity.ok(careGiverService.linkImageToGiver(giverUid, file));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CareGiverResponseDto>> getAllCareGivers() {
        return ResponseEntity.ok(careGiverService.getAll());
    }

    @GetMapping("/getByUid/{uid}")
    public ResponseEntity<CareGiverResponseDto> getCareGiverById(@PathVariable String uid) {
        return ResponseEntity.ok(careGiverService.getByUid(uid));
    }

    @DeleteMapping("/delete/{giverUid}")
    public ResponseEntity<String> deleteGiverByUid (@PathVariable("giverUid") String giverUid){
        return ResponseEntity.ok(careGiverService.deleteGiverByUid(giverUid));
    }

    @GetMapping("/searchByName")
    public ResponseEntity<List<CareGiverResponseDto>> searchByName (@RequestParam String name,  @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(careGiverService.searchByName(name, pageable));
    }

}
