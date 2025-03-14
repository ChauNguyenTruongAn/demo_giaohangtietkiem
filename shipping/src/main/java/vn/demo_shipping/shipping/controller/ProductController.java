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
import vn.demo_shipping.shipping.domain.Product;
import vn.demo_shipping.shipping.dto.request.ProductRequest;
import vn.demo_shipping.shipping.dto.response.APIResponse;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.ServiceException;
import vn.demo_shipping.shipping.service.impl.CategoryServiceImpl;
import vn.demo_shipping.shipping.service.impl.ProductServiceImpl;

@Tag(name = "Sản phẩm")
@RestController
@RequestMapping("/v1/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;

    @Operation(summary = "Lấy tất cả sản phẩm", description = "API này trả về danh sách tất cả các sản phẩm trong hệ thống. Nếu không có sản phẩm nào, trả về mã trạng thái 204 (No Content).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy tất cả sản phẩm thành công"),
            @ApiResponse(responseCode = "204", description = "Không có sản phẩm nào"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy sản phẩm")
    })
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

    @Operation(summary = "Lấy danh sách sản phẩm với phân trang", description = "API này trả về danh sách sản phẩm với phân trang. Các tham số bao gồm số trang, số lượng mỗi trang và thứ tự sắp xếp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách sản phẩm thành công"),
            @ApiResponse(responseCode = "204", description = "Không có sản phẩm nào"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy sản phẩm")
    })
    @GetMapping
    public ResponseEntity<APIResponse<Page<Product>>> getAllProduct(
            @RequestParam(required = false, defaultValue = "0") @Parameter(description = "Số trang cần lấy") Integer page,

            @RequestParam(required = false, defaultValue = "2") @Parameter(description = "Số lượng sản phẩm mỗi trang") Integer size,

            @RequestParam(required = false, defaultValue = "asc") @Parameter(description = "Thứ tự sắp xếp (asc hoặc desc)") String sort) {

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

    @Operation(summary = "Lấy sản phẩm theo ID", description = "API này trả về sản phẩm dựa trên ID cung cấp. Nếu không tìm thấy sản phẩm, sẽ trả về mã lỗi 404.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy sản phẩm thành công"),
            @ApiResponse(responseCode = "404", description = "Sản phẩm không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy sản phẩm")
    })
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

    @Operation(summary = "Tạo sản phẩm mới", description = "API này cho phép tạo một sản phẩm mới với thông tin từ request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo sản phẩm thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ")
    })
    @PostMapping
    private ResponseEntity<APIResponse<Product>> createNewProduct(@Valid @RequestBody ProductRequest request) {
        try {

            Category category = categoryServiceImpl.getCategoryById(request.getCategory_id());
            if (category == null)
                throw new NotFoundException("Category does not exists");

            APIResponse<Product> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new product success",
                    productServiceImpl.addProduct(request), LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Product> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new product success",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Cập nhật sản phẩm", description = "API này cho phép cập nhật thông tin sản phẩm với ID được cung cấp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật sản phẩm thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Sản phẩm không tồn tại")
    })
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

    @Operation(summary = "Xóa sản phẩm", description = "API này cho phép xóa một sản phẩm dựa trên ID cung cấp. Nếu sản phẩm không tồn tại, sẽ trả về mã lỗi 404.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa sản phẩm thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Sản phẩm không tồn tại")
    })
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
