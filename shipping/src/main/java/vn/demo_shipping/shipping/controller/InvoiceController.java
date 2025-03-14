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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Invoice;
import vn.demo_shipping.shipping.domain.InvoiceProductId;
import vn.demo_shipping.shipping.dto.request.InvoiceRequest;
import vn.demo_shipping.shipping.dto.request.OrderDetailRequest;
import vn.demo_shipping.shipping.dto.response.APIResponse;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.ServiceException;
import vn.demo_shipping.shipping.service.impl.InvoiceServiceImpl;

@Tag(name = "Hóa đơn")
@RestController
@RequestMapping("/v1/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceServiceImpl invoiceServiceImpl;

    @Operation(summary = "Lấy tất cả hóa đơn", description = "API này trả về tất cả hóa đơn trong hệ thống. Nếu không có hóa đơn nào, trả về mã trạng thái 204 (No Content).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy tất cả hóa đơn thành công"),
            @ApiResponse(responseCode = "204", description = "Không có hóa đơn nào"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy hóa đơn")
    })
    @GetMapping("/all")
    private ResponseEntity<APIResponse<List<Invoice>>> getAllInvoice() {
        try {
            List<Invoice> categories = invoiceServiceImpl.getAllInvoice();
            if (categories.isEmpty()) {
                APIResponse<List<Invoice>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<List<Invoice>> response = new APIResponse<List<Invoice>>(
                    HttpStatus.OK.value(),
                    "Get all invoice success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<List<Invoice>> response = new APIResponse<List<Invoice>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get invoice fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Lấy hóa đơn theo phân trang", description = "API này trả về danh sách hóa đơn với phân trang. Các tham số bao gồm số trang, số lượng mỗi trang và thứ tự sắp xếp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách hóa đơn thành công"),
            @ApiResponse(responseCode = "204", description = "Không có hóa đơn nào"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy hóa đơn")
    })
    @GetMapping
    private ResponseEntity<APIResponse<Page<Invoice>>> getAllInvoice(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "2") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {

        try {
            Page<Invoice> categories = invoiceServiceImpl.getAllInvoice(page, size, sort);
            if (categories.isEmpty()) {
                APIResponse<Page<Invoice>> response = new APIResponse<>(
                        HttpStatus.NO_CONTENT.value(),
                        "No categories found",
                        categories,
                        LocalDateTime.now());

                return ResponseEntity.ok(response);
            }

            APIResponse<Page<Invoice>> response = new APIResponse<Page<Invoice>>(
                    HttpStatus.OK.value(),
                    "Get all invoice success",
                    categories,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (ServiceException e) {
            APIResponse<Page<Invoice>> response = new APIResponse<Page<Invoice>>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Get invoice fail: " + e.getMessage(), null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Lấy hóa đơn theo ID", description = "API này trả về một hóa đơn dựa trên ID cung cấp. Nếu không tìm thấy hóa đơn, trả về mã lỗi 404.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy hóa đơn thành công"),
            @ApiResponse(responseCode = "404", description = "Hóa đơn không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống khi lấy hóa đơn")
    })
    @GetMapping("/{id}")
    private ResponseEntity<APIResponse<Invoice>> getInvoiceById(@PathVariable Long id) {
        APIResponse<Invoice> response;
        try {
            Invoice invoice = invoiceServiceImpl.getInvoiceById(id);
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get invoice success",
                    invoice,
                    LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            response = new APIResponse<>(
                    HttpStatus.CREATED.value(),
                    "Get invoice fail: " + e.getMessage(),
                    null,
                    LocalDateTime.now());
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Tạo hóa đơn mới", description = "API này cho phép tạo một hóa đơn mới với các thông tin từ request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo hóa đơn thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ")
    })
    @PostMapping
    private ResponseEntity<APIResponse<Invoice>> createNewInvoice(@RequestBody InvoiceRequest request) {
        try {
            Invoice invoice = invoiceServiceImpl
                    .addInvoice(request);
            APIResponse<Invoice> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new invoice success",
                    invoice, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Invoice> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Create a new invoice success",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Cập nhật hóa đơn", description = "API này cho phép cập nhật thông tin của hóa đơn với ID được cung cấp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật hóa đơn thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Hóa đơn không tồn tại")
    })
    @PutMapping("/{id}")
    private ResponseEntity<APIResponse<Invoice>> updateInvoice(@PathVariable Long id,
            @Valid @RequestBody InvoiceRequest request) {
        try {
            Invoice invoice = invoiceServiceImpl.updateInvoice(id, request);
            APIResponse<Invoice> response = new APIResponse<>(HttpStatus.CREATED.value(),
                    "Update a invoice success",
                    invoice, LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<Invoice> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Bad request",
                    null, LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @Operation(summary = "Xóa hóa đơn", description = "API này cho phép xóa một hóa đơn dựa trên ID cung cấp. Nếu hóa đơn không tồn tại, sẽ trả về mã lỗi 404.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa hóa đơn thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Hóa đơn không tồn tại")
    })
    @DeleteMapping("/{id}")
    private ResponseEntity<APIResponse<String>> deleteInvoice(@PathVariable Long id) {
        try {
            Invoice invoice = invoiceServiceImpl.getInvoiceById(id);

            if (invoice == null)
                throw new NotFoundException("Invoice does not exists");

            APIResponse<String> response = new APIResponse<>(HttpStatus.OK.value(), "Failure",
                    "Invoice contains products. Must be remove the products before deleting the invoice!",
                    LocalDateTime.now());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.ok(new APIResponse<String>(HttpStatus.BAD_REQUEST.value(), "Failure",
                    "Not found Invoice", LocalDateTime.now()));
        }

    }

    @Operation(summary = "Thêm sản phẩm vào hóa đơn", description = "API này cho phép thêm một sản phẩm vào hóa đơn dựa trên ID hóa đơn.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Thêm sản phẩm vào hóa đơn thành công"),
            @ApiResponse(responseCode = "404", description = "Hóa đơn không tồn tại")
    })
    @PostMapping("/add-product/{id}")
    private ResponseEntity<APIResponse<Invoice>> addProductIntoInvoice(
            @PathVariable Long id,
            @RequestBody OrderDetailRequest orderDetailRequest) {

        APIResponse<Invoice> response = APIResponse.<Invoice>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created Successful")
                .data(invoiceServiceImpl.addProduct(id, orderDetailRequest))
                .create_time(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Xóa sản phẩm khỏi hóa đơn", description = "API này cho phép xóa một sản phẩm khỏi hóa đơn dựa trên ID sản phẩm.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa sản phẩm khỏi hóa đơn thành công"),
            @ApiResponse(responseCode = "404", description = "Hóa đơn hoặc sản phẩm không tồn tại")
    })
    @DeleteMapping("/remove-product/{id}")
    private ResponseEntity<APIResponse<Invoice>> removeProductFromInvoice(
            @PathVariable Long id,
            @RequestBody InvoiceProductId invoiceProductId) {

        APIResponse<Invoice> response = APIResponse.<Invoice>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Deleted Successful")
                .data(invoiceServiceImpl.removeProduct(id, invoiceProductId))
                .create_time(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}