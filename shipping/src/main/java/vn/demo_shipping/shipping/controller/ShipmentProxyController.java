package vn.demo_shipping.shipping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Cho phép request từ front-end
public class ShipmentProxyController {

    private final RestTemplate restTemplate;

    @Autowired
    public ShipmentProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/proxy/shipment/order")
    public ResponseEntity<?> proxyShipmentOrder(@RequestBody Map<String, Object> payload) {
        String url = "https://services.giaohangtietkiem.vn/services/shipment/order";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Client-Source", "S22863729");
        headers.add("Token", "172X8NEx658WXkBW1oMX7s0SvCl9eODsA53yPmq");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    String.class);
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + ex.getMessage());
        }
    }
}
