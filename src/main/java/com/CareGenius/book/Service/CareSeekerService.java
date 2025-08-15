package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.CareSeekerRequestDto;
import com.CareGenius.book.Dto.CareSeekerResponseDto;
import com.CareGenius.book.Model.User;

import java.util.List;

public interface CareSeekerService {
    CareSeekerResponseDto createCareSeeker(User userDB, CareSeekerRequestDto careSeekerRequestDto);

    List<CareSeekerResponseDto> getAllCareSeeker();

    CareSeekerResponseDto getCareSeekerById(User userDB, String seekerUid);

    String deleteCareSeekerById(String seekerUid);
}
