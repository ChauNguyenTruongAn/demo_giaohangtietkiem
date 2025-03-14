package vn.demo_shipping.shipping.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import vn.demo_shipping.shipping.config.VNPayConfig;

@RestController
@Tag(name = "VNPay Controller")
public class VNPayController {
    @Operation(method = "GET", summary = "Đừng đụng", description = "Cái này đừng đụng tới")
    @GetMapping("/api/vnpay/payment-callback")
    public String paymentCallback(@RequestParam Map<String, String> params) {
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        if ("00".equals(vnp_ResponseCode)) {
            return "Thanh toán thành công!";
        } else {
            return "Thanh toán thất bại!";
        }
    }

    @Operation(method = "GET", summary = "Tạo hóa đơn", description = "Đưa vào cái số tiền và sẽ trả về một cái link đó là link thanh toán.")
    @GetMapping("/api/vnpay/create-payment")
    public String createPayment(HttpServletRequest request, @RequestParam("amount") int amount) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
            String vnp_IpAddr = VNPayConfig.getIpAddress(request);
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // Nhân 100 vì VNPAY yêu cầu số tiền tính theo
                                                                        // VND x100
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", VNPayConfig.getTimeStamp());

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (String fieldName : fieldNames) {
                String value = vnp_Params.get(fieldName);
                if ((value != null) && (!value.isEmpty())) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII))
                            .append('&');
                    query.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII))
                            .append('&');
                }
            }

            String queryUrl = query.substring(0, query.length() - 1);
            String secureHash = hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.substring(0, hashData.length() - 1));
            queryUrl += "&vnp_SecureHash=" + secureHash;
            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

            return paymentUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi tạo thanh toán!";
        }
    }

    public String hmacSHA512(String key, String data) {
        try {
            Mac hmacSHA512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmacSHA512.init(secretKey);
            byte[] hash = hmacSHA512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký HMAC-SHA512", e);
        }
    }
}
