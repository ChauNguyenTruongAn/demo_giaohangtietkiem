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
import vn.demo_shipping.shipping.domain.Category;
import vn.demo_shipping.shipping.domain.Product;
import vn.demo_shipping.shipping.dto.request.ProductRequest;
import vn.demo_shipping.shipping.dto.response.APIResponse;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.ServiceException;
import vn.demo_shipping.shipping.service.impl.CategoryServiceImpl;
import vn.demo_shipping.shipping.service.impl.ProductServiceImpl;

@RestController
@RequestMapping("/v1/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping("/all")
    private ResponseEntity<APIResponse<List<Product>>> getAllProduct() {
        try {
            List<Product> categories = productServiceImpl.getAllProduct();
            if (categories.isEmpty()) {
                APIResponse<List<Product>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<List<Product>> response = new APIResponse<List<Product>>(
                    HttpStatus.OK.value(),
                    "Get all product success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<List<Product>> response = new APIResponse<List<Product>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get product fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    private ResponseEntity<APIResponse<Page<Product>>> getAllProduct(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "2") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {

        try {
            Page<Product> categories = productServiceImpl.getAllProduct(page, size, sort);
            if (categories.isEmpty()) {
                APIResponse<Page<Product>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<Page<Product>> response = new APIResponse<Page<Product>>(
                    HttpStatus.OK.value(),
                    "Get all product success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<Page<Product>> response = new APIResponse<Page<Product>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get product fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<APIResponse<Product>> getProductById(@PathVariable Long id) {
        APIResponse<Product> response;
        try {
            Product product = productServiceImpl.getProductById(id);
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get product success",
                    product,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get product fail: " + e.getMessage(),
                    null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping
    private ResponseEntity<APIResponse<Product>> createNewProduct(@Valid @RequestBody ProductRequest request) {
        try {

            Category category = categoryServiceImpl.getCategoryById(request.getCategory_id());
            if (category == null)
                throw new NotFoundException("Category does not exists");

            Product product = productServiceImpl
                    .addProduct(Product.builder()
                            .name(request.getName())
                            .price(request.getPrice())
                            .weight(request.getWeight())
                            .image(request.getImage().isEmpty() ? "https://via.placeholder.com/150"
                                    : request.getImage())
                            .inventory(request.getInventory())
                            .category(category)
                            .build());
            APIResponse<Product> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new product success",
                    product, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Product> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new product success",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<APIResponse<Product>> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        try {
            Product product = productServiceImpl.updateProduct(id, request);
            APIResponse<Product> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Update a product success",
                    product, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Product> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Bad request",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<APIResponse<String>> deleteProduct(@PathVariable Long id) {
        try {
            String result = productServiceImpl.deleteProduct(id);
            APIResponse<String> response = new APIResponse<>(HttpStatus.OK.value(), "Success",
                    result, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new APIResponse<String>(HttpStatus.BAD_REQUEST.value(), "Failure",
                    "Not found Product", LocalDateTime.now()));
        }
    }

}
