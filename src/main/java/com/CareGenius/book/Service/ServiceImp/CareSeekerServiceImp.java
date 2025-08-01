package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.CareSeekerRequestDto;
import com.CareGenius.book.Dto.CareSeekerResponseDto;
import com.CareGenius.book.Model.CareSeeker;
import com.CareGenius.book.Model.Role;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Repository.CareSeekerRepository;
import com.CareGenius.book.Repository.UserRepository;
import com.CareGenius.book.Service.AIRecommendationService;
import com.CareGenius.book.Service.CareSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class CareSeekerServiceImp implements CareSeekerService {

    private final CareSeekerRepository careSeekerRepository;
    private final UserRepository userRepository;
    private final AIRecommendationService aiRecommendationService;

    @Override
    public CareSeekerResponseDto createCareSeeker(User userDB, CareSeekerRequestDto careSeekerRequestDto) {
        CareSeeker careSeeker = mapToCareSeeker(userDB, careSeekerRequestDto);
        userDB.setRole(Role.SEEKER);

        userRepository.save(userDB);

        careSeekerRepository.save(careSeeker);
        return mapToSeekerResponseDto(careSeeker);
    }

    private CareSeeker mapToCareSeeker(User userDB, CareSeekerRequestDto careSeekerRequestDto) {
        return CareSeeker.builder()
                .careNeedsDescription(careSeekerRequestDto.getCareNeedsDescription())
                .healthConditions(careSeekerRequestDto.getHealthConditions())
                .dob(careSeekerRequestDto.getDob())
                .phoneNumber(careSeekerRequestDto.getPhoneNumber())
                .preferredGiverGender(careSeekerRequestDto.getPreferredGiverGender())
                .user(userDB)
                .build();
    }

    private CareSeekerResponseDto mapToSeekerResponseDto(CareSeeker careSeeker){
        User user = careSeeker.getUser();
        return new CareSeekerResponseDto(
                careSeeker.getUid(),
                user.getFullName(),
                user.getEmail(),
                careSeeker.getDob(),
                careSeeker.getPhoneNumber(),
                careSeeker.getCareNeedsDescription(),
                careSeeker.getHealthConditions(),
                careSeeker.getPreferredGiverGender()
        );
    }
}
