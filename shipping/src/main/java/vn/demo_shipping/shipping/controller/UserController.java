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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.User;
import vn.demo_shipping.shipping.dto.request.AddressRequest;
import vn.demo_shipping.shipping.dto.request.UserRequest;
import vn.demo_shipping.shipping.dto.response.APIResponse;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.ServiceException;
import vn.demo_shipping.shipping.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Operation(summary = "Lấy tất cả người dùng", description = "API này trả về danh sách tất cả người dùng trong hệ thống. Nếu không có người dùng nào, trả về mã trạng thái 204 (No Content).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy tất cả người dùng thành công"),
            @ApiResponse(responseCode = "204", description = "Không có người dùng nào"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy người dùng")
    })
    @GetMapping("/all")
    private ResponseEntity<APIResponse<List<User>>> getAllUser() {
        try {
            List<User> categories = userServiceImpl.getAllUser();
            if (categories.isEmpty()) {
                APIResponse<List<User>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<List<User>> response = new APIResponse<List<User>>(
                    HttpStatus.OK.value(),
                    "Get all user success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<List<User>> response = new APIResponse<List<User>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get user fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Lấy danh sách người dùng với phân trang", description = "API này trả về danh sách người dùng với phân trang. Các tham số bao gồm số trang, số lượng mỗi trang và thứ tự sắp xếp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách người dùng thành công"),
            @ApiResponse(responseCode = "204", description = "Không có người dùng nào"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy người dùng")
    })
    @GetMapping
    public ResponseEntity<APIResponse<Page<User>>> getAllUser(
            @RequestParam(required = false, defaultValue = "0") @Parameter(description = "Số trang cần lấy") Integer page,

            @RequestParam(required = false, defaultValue = "2") @Parameter(description = "Số lượng người dùng mỗi trang") Integer size,

            @RequestParam(required = false, defaultValue = "asc") @Parameter(description = "Thứ tự sắp xếp (asc hoặc desc)") String sort) {

        try {
            Page<User> categories = userServiceImpl.getAllUser(page, size, sort);
            if (categories.isEmpty()) {
                APIResponse<Page<User>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<Page<User>> response = new APIResponse<Page<User>>(
                    HttpStatus.OK.value(),
                    "Get all user success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<Page<User>> response = new APIResponse<Page<User>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get user fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Lấy người dùng theo ID", description = "API này trả về người dùng dựa trên ID cung cấp. Nếu không tìm thấy người dùng, sẽ trả về mã lỗi 404.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy người dùng thành công"),
            @ApiResponse(responseCode = "404", description = "Người dùng không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy người dùng")
    })
    @GetMapping("/{id}")
    private ResponseEntity<APIResponse<User>> getUserById(@PathVariable Long id) {
        APIResponse<User> response;
        try {
            User user = userServiceImpl.getUserById(id);
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get user success",
                    user,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get user fail: " + e.getMessage(),
                    null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Tạo người dùng mới", description = "API này cho phép tạo một người dùng mới với thông tin từ request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo người dùng thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ")
    })
    @PostMapping
    private ResponseEntity<APIResponse<User>> createNewUser(@Valid @RequestBody UserRequest request) {
        try {
            if (request == null)
                throw new IllegalArgumentException("Invalid request");

            APIResponse<User> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new user success",
                    userServiceImpl.addUser(request), LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<User> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new user success",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Cập nhật thông tin người dùng", description = "API này cho phép cập nhật thông tin của người dùng với ID được cung cấp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật người dùng thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Người dùng không tồn tại")
    })
    @PutMapping("/{id}")
    private ResponseEntity<APIResponse<User>> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        try {
            User user = userServiceImpl.updateUser(id, request);
            APIResponse<User> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Update a user success",
                    user, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<User> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Bad request" + e.getMessage(),
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @Operation(summary = "Xóa người dùng", description = "API này cho phép xóa một người dùng dựa trên ID cung cấp. Nếu người dùng không tồn tại, sẽ trả về mã lỗi 404.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa người dùng thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Người dùng không tồn tại")
    })
    @DeleteMapping("/{id}")
    private ResponseEntity<APIResponse<String>> deleteUser(@PathVariable Long id) {
        try {
            String result = userServiceImpl.deleteUser(id);
            APIResponse<String> response = new APIResponse<>(HttpStatus.OK.value(), "Success",
                    result, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new APIResponse<String>(HttpStatus.BAD_REQUEST.value(), "Failure",
                    "Not found User", LocalDateTime.now()));
        }
    }

    @Operation(summary = "Thêm địa chỉ cho người dùng", description = "API này cho phép thêm một địa chỉ mới cho người dùng với ID cung cấp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Thêm địa chỉ thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Người dùng không tồn tại")
    })
    @PostMapping("/add-address/{id}")
    private ResponseEntity<APIResponse<User>> addAddress(@Valid @RequestBody AddressRequest request,
            @PathVariable Long id) {
        try {

            User user = userServiceImpl.getUserById(id);

            request.setUser_id(id);

            if (user == null)
                throw new NotFoundException("User not found");

            APIResponse<User> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Address for user " + id + "created successful",
                    userServiceImpl.addAddressToUser(id, request), LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<User> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Fail to create a new address",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Cập nhật địa chỉ của người dùng", description = "API này cho phép cập nhật địa chỉ của người dùng với ID và địa chỉ ID đã cung cấp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật địa chỉ thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Người dùng hoặc địa chỉ không tồn tại")
    })
    @PutMapping("/edit-address/{id}")
    private ResponseEntity<APIResponse<User>> updateAddress(@PathVariable Long id,
            @RequestParam Long address_id,
            @Valid @RequestBody AddressRequest request) {
        try {
            User user = userServiceImpl.editAddress(id, address_id, request);
            APIResponse<User> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Address for user " + id + " updated successfully",
                    user, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<User> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Bad request",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Xóa địa chỉ của người dùng", description = "API này cho phép xóa địa chỉ của người dùng với ID và địa chỉ ID cung cấp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa địa chỉ thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Người dùng hoặc địa chỉ không tồn tại")
    })
    @DeleteMapping("remove-address/{id}")
    private ResponseEntity<APIResponse<User>> deleteAddress(@PathVariable Long id, @RequestParam Long address_id) {
        try {
            User result = userServiceImpl.removeAddressFromUser(id, address_id);
            APIResponse<User> response = new APIResponse<>(HttpStatus.OK.value(), "Success",
                    result, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new APIResponse<User>(HttpStatus.NO_CONTENT.value(), "Failure",
                            null, LocalDateTime.now()));
        }
    }

}