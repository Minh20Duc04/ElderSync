package com.CareGenius.book.Controller;

import com.CareGenius.book.Dto.UserDto;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> login(@RequestBody UserDto userDto){
        var authenticatedUser = userService.authenticateUser(userDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, (String) authenticatedUser.get("token"))
                .body(authenticatedUser.get("user"));
    }


}
