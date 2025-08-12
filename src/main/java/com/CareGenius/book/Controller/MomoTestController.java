package com.CareGenius.book.Controller;

import com.CareGenius.book.Service.ServiceImp.BookingServiceImp;
import com.CareGenius.book.Service.ServiceImp.MomoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class MomoTestController {

    private final MomoService momoService;

    @GetMapping("/test-momo")
    public String testMomoPayment() {
        try {
            String paymentUrl = momoService.createSandboxMomoLink( 13L,"1000");
            return "Mở link để test: <a href=\"" + paymentUrl + "\">" + paymentUrl + "</a>";
        } catch (Exception e) {
            return "Lỗi khi tạo link thanh toán MoMo: " + e.getMessage();
        }
    }


}
