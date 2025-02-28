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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Warehouse;
import vn.demo_shipping.shipping.dto.request.WarehouseRequest;
import vn.demo_shipping.shipping.dto.response.APIResponse;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.ServiceException;
import vn.demo_shipping.shipping.service.impl.WarehouseServiceImpl;

@RestController
@RequestMapping("/v1/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseServiceImpl warehouseServiceImpl;

     @GetMapping("/all")
    private ResponseEntity<APIResponse<List<Warehouse>>> getAllWarehouse() {
        try {
            List<Warehouse> categories = warehouseServiceImpl.getAllWarehouse();
            if (categories.isEmpty()) {
                APIResponse<List<Warehouse>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<List<Warehouse>> response = new APIResponse<List<Warehouse>>(
                    HttpStatus.OK.value(),
                    "Get all warehouse success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<List<Warehouse>> response = new APIResponse<List<Warehouse>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get warehouse fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    private ResponseEntity<APIResponse<Page<Warehouse>>> getAllWarehouse(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "2") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {

        try {
            Page<Warehouse> categories = warehouseServiceImpl.getAllWarehouse(page, size, sort);
            if (categories.isEmpty()) {
                APIResponse<Page<Warehouse>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<Page<Warehouse>> response = new APIResponse<Page<Warehouse>>(
                    HttpStatus.OK.value(),
                    "Get all warehouse success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<Page<Warehouse>> response = new APIResponse<Page<Warehouse>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get warehouse fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<APIResponse<Warehouse>> getWarehouseById(@PathVariable Long id) {
        APIResponse<Warehouse> response;
        try {
            Warehouse warehouse = warehouseServiceImpl.getWarehouseById(id);
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get warehouse success",
                    warehouse,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get warehouse fail: " + e.getMessage(),
                    null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping
    private ResponseEntity<APIResponse<Warehouse>> createNewWarehouse(@Valid @RequestBody WarehouseRequest request) {
        try {
            if (request == null)
                throw new IllegalArgumentException("Invalid request");

            APIResponse<Warehouse> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new warehouse success",
                    warehouseServiceImpl.addWarehouse(request), LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Warehouse> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new warehouse success",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<APIResponse<Warehouse>> updateWarehouse(@PathVariable Long id,
            @Valid @RequestBody WarehouseRequest request) {
        try {
            Warehouse warehouse = warehouseServiceImpl.updateWarehouse(id, request);
            APIResponse<Warehouse> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Update a warehouse success",
                    warehouse, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Warehouse> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Bad request",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<APIResponse<String>> deleteWarehouse(@PathVariable Long id) {
        try {
            String result = warehouseServiceImpl.deleteWarehouse(id);
            APIResponse<String> response = new APIResponse<>(HttpStatus.OK.value(), "Success",
                    result, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new APIResponse<String>(HttpStatus.BAD_REQUEST.value(), "Failure",
                    "Not found Warehouse", LocalDateTime.now()));
        }
    }
}
