package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.CareSeekerRequestDto;
import com.CareGenius.book.Dto.CareSeekerResponseDto;
import com.CareGenius.book.Model.*;
import com.CareGenius.book.Repository.CareSeekerRepository;
import com.CareGenius.book.Repository.NotificationRepository;
import com.CareGenius.book.Repository.UserRepository;
import com.CareGenius.book.Service.AIRecommendationService;
import com.CareGenius.book.Service.CareSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CareSeekerServiceImp implements CareSeekerService {

    private final CareSeekerRepository careSeekerRepository;
    private final UserRepository userRepository;
    private final AIRecommendationService aiRecommendationService;
    private final NotificationRepository notificationRepository;

    @Override
    public CareSeekerResponseDto createCareSeeker(User userDB, CareSeekerRequestDto careSeekerRequestDto) {
        CareSeeker careSeeker = mapToCareSeeker(userDB, careSeekerRequestDto);
        userDB.setRole(Role.SEEKER);
        userRepository.save(userDB);

        careSeekerRepository.save(careSeeker);

        List<CareGiver> careGiversRecommend = aiRecommendationService.AIRecommendationMatching(careSeeker);
        //neu tao seeker phu hop voi giver luon thi matching
        if(!careGiversRecommend.isEmpty()){
            StringBuilder message = new StringBuilder("Tìm thấy " + careGiversRecommend.size() + " CareGiver phù hợp với yêu cầu của bạn: \n");
            careGiversRecommend.forEach(cgr -> message
                    .append("➤ UID: ").append(cgr.getUid()).append("\n")
                    .append("   Họ tên: ").append(cgr.getUser().getFullName()).append("\n")
                    .append("   SĐT: ").append(cgr.getPhoneNumber()).append("\n")
                    .append("   Email: ").append(cgr.getUser().getEmail()).append("\n")
                    .append("   Ngày sinh: ").append(cgr.getDob()).append("\n\n")
            );

            Notifications notifications = Notifications.builder()
                    .message(message.toString())
                    .type(Type.MATCH_FOUND)
                    .user(userDB)
                    .build();
            notificationRepository.save(notifications);
        }

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
