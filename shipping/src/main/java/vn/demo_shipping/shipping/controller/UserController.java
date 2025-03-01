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

    @GetMapping
    private ResponseEntity<APIResponse<Page<User>>> getAllUser(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "2") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {

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