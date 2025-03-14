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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Category;
import vn.demo_shipping.shipping.dto.request.CategoryRequest;
import vn.demo_shipping.shipping.dto.request.ProductRequest;
import vn.demo_shipping.shipping.dto.response.APIResponse;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.ServiceException;
import vn.demo_shipping.shipping.service.impl.CategoryServiceImpl;

@Tag(name = "Danh mục sản phẩm")
@RestController
@RequestMapping("/v1/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryServiceImpl;

    @Operation(summary = "Lấy tất cả danh mục", description = "API này trả về tất cả danh mục trong hệ thống. Nếu không có danh mục nào, sẽ trả về mã trạng thái 204.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy tất cả danh mục thành công"),
            @ApiResponse(responseCode = "204", description = "Không tìm thấy danh mục nào"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy danh mục")
    })
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

    @Operation(summary = "Lấy danh mục theo phân trang", description = "API này trả về danh mục với hỗ trợ phân trang. Các tham số bao gồm số trang, kích thước mỗi trang và thứ tự sắp xếp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh mục theo trang thành công"),
            @ApiResponse(responseCode = "204", description = "Không tìm thấy danh mục nào"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy danh mục")
    })
    @GetMapping
    private ResponseEntity<APIResponse<Page<Category>>> getAllCategory(
            @RequestParam(required = false, defaultValue = "0") @Parameter(description = "Số trang cần lấy") Integer page,

            @RequestParam(required = false, defaultValue = "2") @Parameter(description = "Số lượng danh mục mỗi trang") Integer size,

            @RequestParam(required = false, defaultValue = "asc") @Parameter(description = "Thứ tự sắp xếp (asc hoặc desc)") String sort) {

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

    @Operation(summary = "Lấy danh mục theo ID", description = "API này trả về một danh mục dựa trên ID cung cấp. Nếu không tìm thấy danh mục, trả về mã lỗi 404.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh mục thành công"),
            @ApiResponse(responseCode = "404", description = "Danh mục không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy danh mục")
    })
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

    @Operation(summary = "Tạo danh mục mới", description = "API này cho phép tạo một danh mục mới với các thông tin từ request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo danh mục thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ")
    })
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

    @Operation(summary = "Cập nhật danh mục", description = "API này cho phép cập nhật thông tin của danh mục với ID được cung cấp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật danh mục thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Danh mục không tồn tại")
    })
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

    @Operation(summary = "Xóa danh mục", description = "API này cho phép xóa một danh mục dựa trên ID cung cấp. Nếu danh mục không tồn tại, sẽ trả về mã lỗi 404.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa danh mục thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Danh mục không tồn tại")
    })
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

    @Operation(summary = "Thêm sản phẩm vào danh mục", description = "API này cho phép thêm một sản phẩm vào danh mục dựa trên ID danh mục.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thêm sản phẩm vào danh mục thành công"),
            @ApiResponse(responseCode = "404", description = "Danh mục hoặc sản phẩm không tồn tại")
    })

    @PostMapping("/add-product/{id}")
    public ResponseEntity<Category> addProductToCategory(
            @PathVariable Long id,
            @RequestBody ProductRequest request) {
        Category updatedCategory = categoryServiceImpl.addProduct(id, request);

        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(summary = "Xóa sản phẩm khỏi danh mục", description = "API này cho phép xóa một sản phẩm khỏi danh mục dựa trên ID sản phẩm.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa sản phẩm khỏi danh mục thành công"),
            @ApiResponse(responseCode = "404", description = "Danh mục hoặc sản phẩm không tồn tại")
    })
    @DeleteMapping("/remove-product/{id}")
    public ResponseEntity<Category> removeProductFromCategory(
            @PathVariable Long id,
            @RequestParam Long productId) {
        Category updatedCategory = categoryServiceImpl.removeCategory(id, productId);

        return ResponseEntity.ok(updatedCategory);
    }
}
