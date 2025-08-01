package com.CareGenius.book.Dto;

import com.CareGenius.book.Model.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class UserResponseDto {

    private String token;

    private String uid;

    private String fullName;

    private String address;

    private String email;

    private String gender;

    private String role;

}
