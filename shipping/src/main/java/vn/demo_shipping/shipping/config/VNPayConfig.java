package vn.demo_shipping.shipping.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import jakarta.servlet.http.HttpServletRequest;

public class VNPayConfig {
    public static final String vnp_TmnCode = "0BPR8WNQ";
    public static final String vnp_HashSecret = "C7SRMVKQKTG4LPF7CM3ASQBM6HG21QS5";
    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String vnp_ReturnUrl = "http://localhost:8080/api/vnpay/payment-callback";
    public static final String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static String getTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return formatter.format(new Date());
    }
}
