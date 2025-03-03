package vn.demo_shipping.shipping.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.demo_shipping.shipping.domain.GHTKOrder;
import vn.demo_shipping.shipping.dto.request.ShippingRequest;
import vn.demo_shipping.shipping.repository.GHTKOrderRepository;

@Service
public class ExternalApiService {
    
    private final RestTemplate restTemplate;
    private final GHTKOrderRepository ghtkOrderRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExternalApiService(RestTemplate restTemplate, GHTKOrderRepository ghtkOrderRepository, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.ghtkOrderRepository = ghtkOrderRepository;
        this.objectMapper = objectMapper;
    }

    public String sendRequest(String token, String clientId, ShippingRequest requestBody) {
        String url = "https://services.giaohangtietkiem.vn/services/shipment/order";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Token", token);
        httpHeaders.set("X-Client-Source", clientId);

        Map<String, Object> body = new HashMap<>();
        body.put("products", requestBody.getProducts());
        body.put("order", requestBody.getOrder());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Chuyển đổi response JSON thành Object
        try {
            JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
            JsonNode orderNode = rootNode.path("order");

            if (!orderNode.isMissingNode()) {
                GHTKOrder order = objectMapper.treeToValue(orderNode, GHTKOrder.class);
                ghtkOrderRepository.save(order); // Lưu vào DB
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseEntity.getBody();
    }
}
