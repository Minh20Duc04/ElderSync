package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.BookingDecision;
import com.CareGenius.book.Dto.BookingDto;
import com.CareGenius.book.Model.*;
import com.CareGenius.book.Repository.*;
import com.CareGenius.book.Service.BookingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DayOfWeek;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor


public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;
    private final CareGiverRepository careGiverRepository;
    private final CareSeekerRepository careSeekerRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public BookingDto createBooking(User userDB, BookingDto bookingDto) {
        User seekerUserDB = userRepository.findById(userDB.getUid()).orElseThrow(); //lay lai user vi cai User trong Authentication la cua Spring Security ko phai cua JPA, khi dang nhap xong se no se mat session nen bi loi lazy load
        //1 la doi thanh Fetch.Eager, 2 la lay lai User ngay trong method
        CareGiver careGiverDB = careGiverRepository.findById(bookingDto.getCareGiverUid()).orElseThrow(() -> new IllegalArgumentException("Giver not found"));
        CareSeeker careSeekerDB = careSeekerRepository.findByUserUid(seekerUserDB.getUid());

        DayOfWeek dayOfFromDate = bookingDto.getFromDate().getDayOfWeek();
        if(!careGiverDB.getSchedule().getDayOfWeeks().contains(dayOfFromDate)){
            throw new IllegalArgumentException("Invalid booking date ah right ?");
        }
        long countBook = bookingRepository.countByCareGiverAndFromDate(careGiverDB, bookingDto.getFromDate());
        if(countBook > 5){
            throw new IllegalArgumentException("Today the giver's schedule is full ");
        }


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
                .user(seekerUserDB)
                .build());

        return mapToBookingDto(booking);
    }

    @Override
    public String decideBooking(User userDB, BookingDecision bookingDecision) throws Exception {
        Booking bookingDB = bookingRepository.findById(bookingDecision.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Not found booking"));

        CareGiver careGiverDB = careGiverRepository.findByUser(userDB);
        CareSeeker careSeekerDB = careSeekerRepository.findByUser(userDB);

        // Kiểm tra quyền
        boolean isGiver = careGiverDB != null && careGiverDB.equals(bookingDB.getCareGiver());
        boolean isSeeker = careSeekerDB != null && careSeekerDB.equals(bookingDB.getCareSeeker());

        if (!isGiver && !isSeeker) {
            throw new IllegalArgumentException("You do not have permission to update this booking");
        }

        // Nếu Giver confirm booking
        if (bookingDecision.getType().equals(Type.BOOKING_CONFIRMED)) {
            if (!isGiver) {
                throw new IllegalArgumentException("Only Giver can confirm the booking");
            }

            bookingDB.setMeetingLink(bookingDecision.getMeetingLink());
            bookingDB.setType(Type.BOOKING_CONFIRMED);
            bookingRepository.save(bookingDB);

            notificationRepository.save(Notifications.builder()
                    .type(Type.BOOKING_CONFIRMED)
                    .user(bookingDB.getCareSeeker().getUser()) // lấy từ booking luôn
                    .message("Booking của bạn đã được Giver xác nhận thành công, link họp: "
                            + bookingDB.getMeetingLink())
                    .build());

            sendEmail(bookingDB.getCareSeeker().getUser().getEmail(), bookingDB);

        } else if (bookingDecision.getType().equals(Type.BOOKING_CANCELED)) {
            bookingRepository.delete(bookingDB);
        }

        return "Updated this booking successfully";
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

    private void sendEmail(String email, Booking booking) throws Exception {
        String momoPaymentLink = null;

        if(booking.getPayment().equals(Payment.ONLINE)){
            momoPaymentLink = createSandboxMomoLink(booking.getId(), booking.getCareGiver().getFee().toString());
        }

        String subject = "ElderSync - Xác nhận booking #" + booking.getId();

        String body = String.format(
                "Xin chào %s,\n\n" +
                        "Booking của bạn đã được Giver xác nhận thành công!\n" +
                        "📅 Ngày: %s\n" +
                        "🕒 Thời gian: %s - %s\n" +
                        "💳 Hình thức thanh toán: %s\n" +
                        "🔗 Link họp: %s\n\n" +
                        "Vui lòng truy cập link đúng giờ. Cảm ơn bạn đã sử dụng ElderSync. ",
                booking.getCareSeeker().getUser().getFullName(),
                booking.getFromDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getPayment() + ((momoPaymentLink.isEmpty()) ? " " : momoPaymentLink),
                booking.getMeetingLink()
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(fromEmail);

        mailSender.send(message);
    }

    public String createSandboxMomoLink(Long bookingId, String amount) throws Exception {
        String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";

        String partnerCode = "MOMOXXXXX"; // từ momo
        String accessKey = "F8BBA842ECF85";
        String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";

        String redirectUrl = "https://yourdomain.com/payment-success";
        String ipnUrl = "https://yourdomain.com/payment-ipn";
        String orderId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();
        String orderInfo = "Thanh toán booking #" + bookingId;
        String requestType = "captureWallet";
        String extraData = "";

        // Raw data for HMAC SHA256
        String rawHash = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        // Tạo chữ ký
        String signature = hmacSHA256(rawHash, secretKey);

        // Build JSON body
        Map<String, String> payload = new HashMap<>();
        payload.put("partnerCode", partnerCode);
        payload.put("accessKey", accessKey);
        payload.put("requestId", requestId);
        payload.put("amount", amount);
        payload.put("orderId", orderId);
        payload.put("orderInfo", orderInfo);
        payload.put("redirectUrl", redirectUrl);
        payload.put("ipnUrl", ipnUrl);
        payload.put("extraData", extraData);
        payload.put("requestType", requestType);
        payload.put("signature", signature);
        payload.put("lang", "vi");

        // Gửi POST đến MOMO
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(payload)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Trích xuất payUrl
        JsonNode jsonNode = new ObjectMapper().readTree(response.body());
        return jsonNode.get("payUrl").asText(); // Đây là đường dẫn đến sandbox momo
    }

    private String hmacSHA256(String data, String key) {
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            hmacSha256.init(secretKeySpec);
            byte[] hash = hmacSha256.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
    }
}
