package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.UserDto;
import com.CareGenius.book.Dto.UserResponseDto;
import com.CareGenius.book.Model.User;

import java.util.Map;

public interface UserService {
    User register(UserDto userDto);

    UserResponseDto authenticateUser(UserDto userDto);

    String sendResetPasswordEmail(String email);
}
