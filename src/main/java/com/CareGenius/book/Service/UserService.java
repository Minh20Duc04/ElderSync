package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.UserDto;
import com.CareGenius.book.Model.User;

public interface UserService {
    User register(UserDto userDto);
}
