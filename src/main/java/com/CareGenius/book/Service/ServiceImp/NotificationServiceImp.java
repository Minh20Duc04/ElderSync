package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.CareGiverResponseDto;
import com.CareGenius.book.Dto.NotificationsDto;
import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.Notifications;
import com.CareGenius.book.Model.User;
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

        switch (ntf.getType()) {
            case MATCH_FOUND:
                // Nếu là CareSeeker thì lấy danh sách CareGiver được match
                List<CareGiverResponseDto> careGiverResponseDtos = null;

                if(userDB.getCareSeeker() != null && userDB.getCareSeeker().getRecommendations() != null) {
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
                        "Tôi tìm thấy các dữ liệu sau cho bạn:",
                        type,
                        createdAt,
                        careGiverResponseDtos
                );

            case BOOKING_CONFIRMED:
                //làm cho Booking sau này
                return new NotificationsDto(
                        "Lịch hẹn của bạn đã được xác nhận.",
                        type,
                        createdAt,
                        null
                );

            case BOOKING_CANCELED:
                return new NotificationsDto(
                        "Lịch hẹn của bạn đã bị hủy.",
                        type,
                        createdAt,
                        null
                );

            case NEW_MESSAGE:
                // làm cho message sau này
                return new NotificationsDto(
                        "Bạn có tin nhắn mới.",
                        type,
                        createdAt,
                        null
                );

            case REVIEW_RECEIVED:
                // làm cho review sau này
                return new NotificationsDto(
                        "Bạn vừa nhận được đánh giá từ người dùng.",
                        type,
                        createdAt,
                        null
                );

            case PAYMENT_RECEIVED:
                // làm cho booking sau này
                return new NotificationsDto(
                        "Bạn đã nhận được một khoản thanh toán.",
                        type,
                        createdAt,
                        null
                );

            case TRAINING_AVAILABLE:
                // làm cho training sau này
                return new NotificationsDto(
                        "Một khóa đào tạo mới đang sẵn có cho bạn.",
                        type,
                        createdAt,
                        null
                );

            default:
                return new NotificationsDto(
                        "Thông báo không xác định",
                        type,
                        createdAt,
                        null
                );
        }
    }



}
