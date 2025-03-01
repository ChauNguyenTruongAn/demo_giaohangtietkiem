package vn.demo_shipping.shipping.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.dto.request.ShippingRequest;

@RequiredArgsConstructor
public class ExternalApiService {
    private final RestTemplate restTemplate;

    public String sendRequest(String token, String clientId, ShippingRequest requestBody) {
        String url = "https://services.giaohangtietkiem.vn/services/shipment/order";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Token", "2ea86fdf4850e544f1074d2a38cd5ade0144f3bb");
        httpHeaders.set("X-Client-Source", "S22863729");

        Map<String, Object> body = new HashMap<>();
        body.put("products", requestBody.getProducts());
        body.put("order", requestBody.getOrder());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return responseEntity.getBody();

    }
}
