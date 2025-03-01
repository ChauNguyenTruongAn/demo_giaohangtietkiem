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
import vn.demo_shipping.shipping.dto.request.CategoryRequest;
import vn.demo_shipping.shipping.dto.request.ProductRequest;
import vn.demo_shipping.shipping.dto.response.APIResponse;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.ServiceException;
import vn.demo_shipping.shipping.service.impl.CategoryServiceImpl;

@RestController
@RequestMapping("/v1/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping("/all")
    private ResponseEntity<APIResponse<List<Category>>> getAllCategory() {
        try {
            List<Category> categories = categoryServiceImpl.getAllCategory();
            if (categories.isEmpty()) {
                APIResponse<List<Category>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<List<Category>> response = new APIResponse<List<Category>>(
                    HttpStatus.OK.value(),
                    "Get all category success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<List<Category>> response = new APIResponse<List<Category>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get category fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    private ResponseEntity<APIResponse<Page<Category>>> getAllCategory(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "2") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {

        try {
            Page<Category> categories = categoryServiceImpl.getAllCategory(page, size, sort);
            if (categories.isEmpty()) {
                APIResponse<Page<Category>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<Page<Category>> response = new APIResponse<Page<Category>>(
                    HttpStatus.OK.value(),
                    "Get all category success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<Page<Category>> response = new APIResponse<Page<Category>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get category fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<APIResponse<Category>> getCategoryById(@PathVariable Long id) {
        APIResponse<Category> response;
        try {
            Category category = categoryServiceImpl.getCategoryById(id);
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get category success",
                    category,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get category fail: " + e.getMessage(),
                    null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping
    private ResponseEntity<APIResponse<Category>> createNewCategory(@Valid @RequestBody CategoryRequest request) {
        try {

            APIResponse<Category> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new category success",
                    categoryServiceImpl.addCategory(request), LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Category> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new category success",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<APIResponse<Category>> updateCategory(@PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        try {
            Category category = categoryServiceImpl.updateCategory(id, request);
            APIResponse<Category> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Update a category success",
                    category, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Category> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Bad request",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<APIResponse<String>> deleteCategory(@PathVariable Long id) {
        try {
            Category category = categoryServiceImpl.getCategoryById(id);

            if (category == null)
                throw new NotFoundException("Category does not exists");

            String result = categoryServiceImpl.deleteCategory(id);
            APIResponse<String> response = new APIResponse<>(HttpStatus.OK.value(), "Success",
                    result, LocalDateTime.now());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.ok(new APIResponse<String>(HttpStatus.BAD_REQUEST.value(), "Failure",
                    "Not found Category", LocalDateTime.now()));
        }
    }

    @PostMapping("/add-product/{id}")
    public ResponseEntity<Category> addProductToCategory(
            @PathVariable Long id,
            @RequestBody ProductRequest request) {
        Category updatedCategory = categoryServiceImpl.addProduct(id, request);

        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/remove-product/{id}")
    public ResponseEntity<Category> removeProductFromCategory(
            @PathVariable Long id,
            @RequestParam Long productId) {
        Category updatedCategory = categoryServiceImpl.removeCategory(id, productId);

        return ResponseEntity.ok(updatedCategory);
    }
}
