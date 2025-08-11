package com.CareGenius.book.Controller;

import com.CareGenius.book.Dto.UserDto;
import com.CareGenius.book.Dto.UserResponseDto;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserDto userDto) {
        var userResponseDto = userService.authenticateUser(userDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, userResponseDto.getToken())
                .body(userResponseDto);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email){
        return ResponseEntity.ok(userService.sendResetPasswordEmail(email));
    }

    @DeleteMapping("/delete/{userUid}")
    public ResponseEntity<String> deleteGiverByUid (@PathVariable("userUid") String userUid){
        return ResponseEntity.ok(userService.deleteUserByUid(userUid));
    }
}
