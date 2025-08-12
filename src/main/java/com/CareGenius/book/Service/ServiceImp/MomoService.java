package com.CareGenius.book.Service.ServiceImp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MomoService {

    @Value("${partnerCode}")
    private String partnerCode;

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;

    // shout out cho ô https://github.com/zagkun2k vì 3 biến partnerCode, accessKey, secretKey phải tạo tk doanh nghiệp mới có
    public String createSandboxMomoLink(Long bookingId, String amount) throws Exception {
        String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
        String redirectUrl = "https://yourdomain.com/payment-success";
        String ipnUrl = "https://yourdomain.com/payment-ipn";
        String orderId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();
        String orderInfo = "Thanh toán booking #" + bookingId;
        String requestType = "captureWallet";
        String extraData = "";

        // Đảm bảo amount là số nguyên (tại vi lúc đầu có số 20 triệu Momo không nhận số có dấu phẩy)
        String amountStr = new java.math.BigDecimal(amount)
                .stripTrailingZeros()
                .toPlainString();

        // Raw data for HMAC SHA256
        String rawHash = "accessKey=" + accessKey +
                "&amount=" + amountStr +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        String signature = hmacSHA256(rawHash, secretKey);

        // Build JSON body
        Map<String, String> payload = new HashMap<>();
        payload.put("partnerCode", partnerCode);
        payload.put("accessKey", accessKey);
        payload.put("requestId", requestId);
        payload.put("amount", amountStr);
        payload.put("orderId", orderId);
        payload.put("orderInfo", orderInfo);
        payload.put("redirectUrl", redirectUrl);
        payload.put("ipnUrl", ipnUrl);
        payload.put("extraData", extraData);
        payload.put("requestType", requestType);
        payload.put("signature", signature);
        payload.put("lang", "vi");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(payload)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Debug log phản hồi của MoMo
        System.out.println("MoMo response: " + response.body());

        // Parse JSON trả về
        JsonNode jsonNode = new ObjectMapper().readTree(response.body());

        if (jsonNode.has("payUrl") && !jsonNode.get("payUrl").isNull()) {
            return jsonNode.get("payUrl").asText();
        } else {
            throw new RuntimeException("MoMo trả lỗi: " + jsonNode.toPrettyString());
        }
    }


    private String hmacSHA256(String data, String key) {
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secretKeySpec);
            byte[] hash = hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Bị lỗi chữ ký, chuyễn sang Hex chứ ko để Base64
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
    }
}
