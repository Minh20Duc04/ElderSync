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

public class UserDto {

    private String firstName;

    private String lastName;

    private String address;

    private String email;

    private String password;

    private Gender gender;

}
