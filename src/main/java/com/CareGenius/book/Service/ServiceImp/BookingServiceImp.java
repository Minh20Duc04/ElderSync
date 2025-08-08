package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.BookingDto;
import com.CareGenius.book.Model.*;
import com.CareGenius.book.Repository.BookingRepository;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.CareSeekerRepository;
import com.CareGenius.book.Repository.NotificationRepository;
import com.CareGenius.book.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor


public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;
    private final CareGiverRepository careGiverRepository;
    private final CareSeekerRepository careSeekerRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public BookingDto createBooking(User userDB, BookingDto bookingDto) {
        CareGiver careGiverDB = careGiverRepository.findById(bookingDto.getCareGiverUid()).orElseThrow(() -> new IllegalArgumentException("Giver not found"));
        CareSeeker careSeekerDB = careSeekerRepository.findByUserUid(userDB.getUid());

        if (bookingDto.getCarelocation().equals(Carelocation.CLINIC)) {
            bookingDto.setStartTime(careGiverDB.getSchedule().getStartTime());
            bookingDto.setEndTime(careGiverDB.getSchedule().getEndTime());
        }

        Booking booking = mapToBooking(bookingDto, careGiverDB, careSeekerDB);
        bookingRepository.save(booking);

        //Tao notify cho giver
        notificationRepository.save(Notifications.builder()
                .message("Bạn vừa nhận được yêu cầu chăm sóc mới từ: " + userDB.getFullName())
                .type(Type.BOOKING_PENDING)
                .user(careGiverDB.getUser())
                .build());

        //Tao notify cho seeker
        notificationRepository.save(Notifications.builder()
                .message("Yêu cầu chăm sóc của bạn đã được gửi thành công! Vui lòng đợi người chăm sóc xác nhận.")
                .type(Type.BOOKING_PENDING)
                .user(userDB)
                .build());

        return mapToBookingDto(booking);
    }

    private Booking mapToBooking(BookingDto bookingDto, CareGiver careGiverDB, CareSeeker careSeekerDB) {
        return Booking.builder()
                .careLocation(bookingDto.getCarelocation())
                .fromDate(bookingDto.getFromDate())
                .duration(bookingDto.getDuration())
                .type(Type.BOOKING_PENDING)
                .startTime(bookingDto.getStartTime())
                .endTime(bookingDto.getEndTime())
                .careGiver(careGiverDB)
                .careSeeker(careSeekerDB)
                .note(bookingDto.getNote())
                .meetingLink("Wait till Giver confirm your request")
                .payment(bookingDto.getPayment())
                .build();
    }


    private BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(
                booking.getCareLocation(),
                booking.getFromDate(),
                booking.getDuration(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getNote(),
                booking.getPayment(),
                booking.getCareGiver().getUid()
        );
    }
}
