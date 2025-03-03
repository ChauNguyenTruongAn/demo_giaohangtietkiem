package vn.demo_shipping.shipping.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Order;
import vn.demo_shipping.shipping.dto.request.OrderRequest;
import vn.demo_shipping.shipping.dto.response.APIResponse;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.ServiceException;
import vn.demo_shipping.shipping.repository.GHTKOrderRepository;
import vn.demo_shipping.shipping.service.impl.ExternalApiService;
import vn.demo_shipping.shipping.service.impl.OrderServiceImpl;

@RestController
@RequestMapping("/v1/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;
    private final GHTKOrderRepository ghtkOrderRepository;

    @GetMapping("/all")
    private ResponseEntity<APIResponse<List<Order>>> getAllOrder() {
        try {
            List<Order> categories = orderServiceImpl.getAllOrder();
            if (categories.isEmpty()) {
                APIResponse<List<Order>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<List<Order>> response = new APIResponse<List<Order>>(
                    HttpStatus.OK.value(),
                    "Get all order success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<List<Order>> response = new APIResponse<List<Order>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get order fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    private ResponseEntity<APIResponse<Page<Order>>> getAllOrder(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "2") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {

        try {
            Page<Order> categories = orderServiceImpl.getAllOrder(page, size, sort);
            if (categories.isEmpty()) {
                APIResponse<Page<Order>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<Page<Order>> response = new APIResponse<Page<Order>>(
                    HttpStatus.OK.value(),
                    "Get all order success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<Page<Order>> response = new APIResponse<Page<Order>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get order fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<APIResponse<Order>> getOrderById(@PathVariable Long id) {
        APIResponse<Order> response;
        try {
            Order order = orderServiceImpl.getOrderById(id);
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get order success",
                    order,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get order fail: " + e.getMessage(),
                    null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping
    private ResponseEntity<APIResponse<Order>> createNewOrder(@Valid @RequestBody OrderRequest request) {
        try {
            Order order = orderServiceImpl
                    .addOrder(request);
            APIResponse<Order> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Successfully created a new order.",
                    order, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Order> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "New order created failure",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<APIResponse<Order>> updateOrder(@PathVariable Long id,
            @Valid @RequestBody OrderRequest request) {
        try {
            Order order = orderServiceImpl.updateOrder(id, request);
            APIResponse<Order> response = new APIResponse<>(HttpStatus.OK.value(),
                    "Successfully updated an order",
                    order, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Order> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Bad request",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<APIResponse<String>> deleteOrder(@PathVariable Long id) {
        try {
            Order order = orderServiceImpl.getOrderById(id);

            if (order == null)
                throw new NotFoundException("Order does not exists");

            APIResponse<String> response = new APIResponse<>(HttpStatus.OK.value(), "Successful",
                    orderServiceImpl.deleteOrder(id),
                    LocalDateTime.now());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.ok(new APIResponse<String>(HttpStatus.BAD_REQUEST.value(), "Failure",
                    "Not found Order", LocalDateTime.now()));
        }

    }

    @PostMapping("/shipping-order")
    private ResponseEntity<String> shippingOrder(@Valid @RequestBody OrderRequest request) {

        RestTemplate template = new RestTemplate();
        ExternalApiService externalApiService = new ExternalApiService(
                template, ghtkOrderRepository, new ObjectMapper());

        String response = externalApiService.sendRequest("2ea86fdf4850e544f1074d2a38cd5ade0144f3bb", "S22863729",
                orderServiceImpl.shippingOrder(request));

        return ResponseEntity.status(201).body(response);
    }

}
