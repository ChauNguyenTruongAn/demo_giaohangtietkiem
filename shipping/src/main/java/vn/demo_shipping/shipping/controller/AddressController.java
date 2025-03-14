package vn.demo_shipping.shipping.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Address;
import vn.demo_shipping.shipping.dto.request.AddressRequest;
import vn.demo_shipping.shipping.dto.response.APIResponse;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.ServiceException;
import vn.demo_shipping.shipping.service.impl.AddressServiceImpl;

@Tag(name = "Địa chỉ")
@RestController
@RequestMapping("/v1/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressServiceImpl addressServiceImpl;

    @GetMapping("/all")
    @Operation(method = "GET", summary = "Get a list of addresses", description = "Return a entire addresses in system.")
    private ResponseEntity<APIResponse<List<Address>>> getAllAddress() {
        try {
            List<Address> categories = addressServiceImpl.getAllAddress();
            if (categories.isEmpty()) {
                APIResponse<List<Address>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "Address not found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<List<Address>> response = new APIResponse<List<Address>>(
                    HttpStatus.OK.value(),
                    "Get all address success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<List<Address>> response = new APIResponse<List<Address>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Fail to retrieve addresses: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    @Operation(method = "GET", summary = "Get a list of addresses", description = "Retrieve a paginated list of address, with support for pagination and sort")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieve of addresses"),
            @ApiResponse(responseCode = "204", description = "No addresses found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    private ResponseEntity<APIResponse<Page<Address>>> getAllAddress(
            @Parameter(description = "The page number to retrieve") @RequestParam(required = false, defaultValue = "0") Integer page,
            @Parameter(description = "The number of address per page") @RequestParam(required = false, defaultValue = "2") Integer size,
            @Parameter(description = "Sorting order of the address, either 'asc' or 'desc'") @RequestParam(required = false, defaultValue = "asc") String sort) {

        try {
            Page<Address> address = addressServiceImpl.getAllAddress(page, size, sort);
            if (address.isEmpty()) {
                APIResponse<Page<Address>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No address found",
                        address,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<Page<Address>> response = new APIResponse<Page<Address>>(
                    HttpStatus.OK.value(),
                    "Get all address success",
                    address,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<Page<Address>> response = new APIResponse<Page<Address>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Fail to retrieve addresses: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(method = "GET", summary = "Get address by ID", description = "Return specific address based on the provided ID. If not found, return 404 error!")
    private ResponseEntity<APIResponse<Address>> getAddressById(@PathVariable Long id) {
        APIResponse<Address> response;
        try {
            Address product = addressServiceImpl.getAddressById(id);
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get address success",
                    product,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            response = new APIResponse<>(
                    HttpStatus.NO_CONTENT.value(),
                    "Address not found: " + e.getMessage(),
                    null,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // @PostMapping
    // private ResponseEntity<APIResponse<Address>> createNewAddress(@Valid
    // @RequestBody AddressRequest request) {
    // try {

    // User user = userServiceImpl.getUserById(request.getUser_id());
    // if (user == null)
    // throw new NotFoundException("User not found");

    // Address product = addressServiceImpl
    // .addAddress(
    // Address.builder()
    // .province(request.getProvince())
    // .district(request.getDistrict())
    // .ward(request.getWard())
    // .address(request.getAddress())
    // .street(request.getStreet())
    // .hamlet(request.getHamlet())
    // .user(user)
    // .build());
    // APIResponse<Address> response = new APIResponse<>(HttpStatus.CREATED.value(),
    // "Create a new address success",
    // product, LocalDateTime.now());
    // return ResponseEntity.ok(response);
    // } catch (Exception e) {
    // APIResponse<Address> response = new APIResponse<>(HttpStatus.CREATED.value(),
    // "Fail to create new address",
    // null, LocalDateTime.now());
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    // }
    // }

    @PutMapping("/{id}")
    @Operation(method = "PUT", summary = "Chỉnh sửa địa chỉ", description = "Chỉnh sửa một địa chỉ dựa trên id, nếu sai trả về mã lỗi 404")
    private ResponseEntity<APIResponse<Address>> updateAddress(@PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {
        try {
            Address product = addressServiceImpl.updateAddress(id, request);
            APIResponse<Address> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Update a product success",
                    product, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Address> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Bad request",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(method = "DELETE", summary = "Xóa địa chỉ", description = "Xóa địa chỉ dựa theo id cụ thể")
    private ResponseEntity<APIResponse<String>> deleteAddress(@PathVariable Long id) {
        try {
            String result = addressServiceImpl.deleteAddress(id);
            APIResponse<String> response = new APIResponse<>(HttpStatus.OK.value(), "Success",
                    result, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new APIResponse<String>(HttpStatus.BAD_REQUEST.value(), "Failure",
                    "Not found Address", LocalDateTime.now()));
        }
    }

}
