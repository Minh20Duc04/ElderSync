package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.CareSeekerRequestDto;
import com.CareGenius.book.Dto.CareSeekerResponseDto;
import com.CareGenius.book.Model.*;
import com.CareGenius.book.Repository.BookingRepository;
import com.CareGenius.book.Repository.CareSeekerRepository;
import com.CareGenius.book.Repository.NotificationRepository;
import com.CareGenius.book.Repository.UserRepository;
import com.CareGenius.book.Service.AIRecommendationService;
import com.CareGenius.book.Service.BookingService;
import com.CareGenius.book.Service.CareSeekerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareSeekerServiceImp implements CareSeekerService {

    private final CareSeekerRepository careSeekerRepository;
    private final UserRepository userRepository;
    private final AIRecommendationService aiRecommendationService;
    private final NotificationRepository notificationRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public CareSeekerResponseDto createCareSeeker(User userDB, CareSeekerRequestDto careSeekerRequestDto) { //truong hop seeker muon dat dich vu lai
        CareSeeker careSeeker = null;

        if(userDB.getRole() != Role.SEEKER){
            careSeeker = mapToCareSeeker(userDB, careSeekerRequestDto);
            userDB.setRole(Role.SEEKER);
        } else{ //dob, careneed, heath, preferGen, phoneNum
            careSeeker = careSeekerRepository.findByUserUid(userDB.getUid());
            if(careSeeker == null){
                careSeeker = mapToCareSeeker(userDB, careSeekerRequestDto); //truong hop neu xoa seeker cu ma ko set lai role thi phai map cai moi
            }

            careSeeker.setDob(careSeekerRequestDto.getDob());
            careSeeker.setCareNeedsDescription(new HashSet<>(careSeekerRequestDto.getCareNeedsDescription()));
            careSeeker.setHealthConditions(new HashSet<>(careSeekerRequestDto.getHealthConditions()));
            careSeeker.setPreferredGiverGender(careSeekerRequestDto.getPreferredGiverGender());
            careSeeker.setPhoneNumber(careSeekerRequestDto.getPhoneNumber());

        }
        userRepository.save(userDB);
        //neu seeker da co thi user
        careSeekerRepository.save(careSeeker);

        List<CareGiver> careGiversRecommend = aiRecommendationService.AIRecommendationMatching(careSeeker);
        //neu tao seeker phu hop voi giver luon thi matching
        if(!careGiversRecommend.isEmpty()){
            StringBuilder message = new StringBuilder("Tìm thấy " + careGiversRecommend.size() + " CareGiver phù hợp với yêu cầu của bạn: \n");
            careGiversRecommend.forEach((cgr) -> message
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

    @Override
    public List<CareSeekerResponseDto> getAllCareSeeker() {
        return careSeekerRepository.findAll().stream().map(this::mapToSeekerResponseDto).collect(Collectors.toList());
    }

    @Override
    public CareSeekerResponseDto getCareSeekerById(User userDB, String seekerUid) {
        //seeker xem profile phải đúng uid, giver muốn xem seeker thì phải có booking đã
        boolean isSeeker = userDB.getCareSeeker() != null && userDB.getCareSeeker().getUid().equals(seekerUid);

        boolean isGiverBooking = userDB.getCareGiver() != null && bookingRepository.existsByCareSeeker_UidAndCareGiver_Uid(seekerUid, userDB.getCareGiver().getUid());

        if(!isSeeker && !isGiverBooking && !userDB.getRole().equals(Role.ADMIN)){
            throw new RuntimeException("Don't snoop on others !");
        }
        return mapToSeekerResponseDto(careSeekerRepository.findById(seekerUid).orElseThrow(()-> new IllegalArgumentException("Not found seeker")));
    }

    @Override
    public String deleteCareSeekerById(String seekerUid) {
        careSeekerRepository.deleteById(seekerUid);
        return "Deleted seeker with uid: " + seekerUid;
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
        System.out.println(careSeeker.getHealthConditions() + " " + careSeeker.getCareNeedsDescription());
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
