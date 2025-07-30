package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.CareSeekerRequestDto;
import com.CareGenius.book.Dto.CareSeekerResponseDto;
import com.CareGenius.book.Model.User;

public interface CareSeekerService {
    CareSeekerResponseDto createCareSeeker(User userDB, CareSeekerRequestDto careSeekerRequestDto);
}
