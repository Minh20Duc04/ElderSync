package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.CareGiverResponseDto;
import com.CareGenius.book.Dto.NotificationsDto;
import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.Notifications;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.CareSeekerRepository;
import com.CareGenius.book.Repository.NotificationRepository;
import com.CareGenius.book.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final CareSeekerRepository careSeekerRepository;
    private final CareGiverRepository careGiverRepository;

    @Override
    public List<NotificationsDto> getNotification(User userDB) {
        List<Notifications> notifiDB = notificationRepository.findByUserOrderByCreatedAtDesc(userDB);

        return notifiDB.stream()
                .map((ntf) -> buildDtoByType(ntf, userDB))
                .collect(Collectors.toList());
    }

    private NotificationsDto buildDtoByType(Notifications ntf, User userDB) {
        String type = ntf.getType().name();
        LocalDate createdAt = ntf.getCreatedAt();
        String message = ntf.getMessage();

        switch (ntf.getType()) {
            case MATCH_FOUND:
                // Nếu là CareSeeker thì lấy danh sách CareGiver được match
                List<CareGiverResponseDto> careGiverResponseDtos = null;

                if (userDB.getCareSeeker() != null && userDB.getCareSeeker().getRecommendations() != null) {
                    careGiverResponseDtos = userDB.getCareSeeker().getRecommendations().stream()
                            .map((rec) -> {
                                CareGiver cgd = rec.getCareGiver();
                                return new CareGiverResponseDto(
                                        cgd.getUid(),
                                        cgd.getUser().getFullName(),
                                        cgd.getUser().getGender().name(),
                                        cgd.getImageUrl(),
                                        cgd.getYearExperience(),
                                        cgd.getFee(),
                                        null, null, null, null
                                );
                            })
                            .collect(Collectors.toList());
                }

                return new NotificationsDto(
                        "Tôi tìm thấy các dữ liệu sau cho bạn: ",
                        type,
                        createdAt,
                        careGiverResponseDtos
                );

            case BOOKING_PENDING:
            case BOOKING_CONFIRMED:
            case BOOKING_CANCELED:
            case NEW_MESSAGE:
            case REVIEW_RECEIVED:
            case PAYMENT_RECEIVED:
            case TRAINING_AVAILABLE:
            default:
                return new NotificationsDto(
                        message != null ? message : "Thông báo không xác định",
                        type,
                        createdAt,
                        null
                );
        }
    }



}
