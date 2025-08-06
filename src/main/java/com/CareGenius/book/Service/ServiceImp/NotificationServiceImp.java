package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.CareGiverResponseDto;
import com.CareGenius.book.Dto.NotificationsDto;
import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.Gender;
import com.CareGenius.book.Model.Notifications;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Repository.NotificationRepository;
import com.CareGenius.book.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;


    @Override
    public List<NotificationsDto> getNotification(User userDB) {
        List<Notifications> notifiDB = notificationRepository.findByUserOrderByCreatedAtDesc(userDB);

        List<CareGiver> careGivers = userDB.getCareSeeker().getRecommendations().stream().map((cgd)->
                cgd.getCareGiver()).collect(Collectors.toList());

        List<CareGiverResponseDto> careGiverResponseDtos = careGivers.stream().map((cgd)->
                new CareGiverResponseDto(cgd.getUid(), cgd.getUser().getFullName(), cgd.getUser().getGender().name(), cgd.getImageUrl(), cgd.getYearExperience(), cgd.getFee(), null, null, null, null)).collect(Collectors.toList());

        List<NotificationsDto> notifiDtos = notifiDB.stream().map((ntf)->
                new NotificationsDto(ntf.getMessage(),ntf.getType().name(),ntf.getCreatedAt(), careGiverResponseDtos)).collect(Collectors.toList());

        return notifiDtos;
    }


}
