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
import vn.demo_shipping.shipping.domain.Invoice;
import vn.demo_shipping.shipping.domain.InvoiceProductId;
import vn.demo_shipping.shipping.domain.OrderDetail;
import vn.demo_shipping.shipping.dto.request.InvoiceRequest;
import vn.demo_shipping.shipping.dto.request.OrderDetailRequest;
import vn.demo_shipping.shipping.dto.response.APIResponse;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.ServiceException;
import vn.demo_shipping.shipping.service.impl.InvoiceServiceImpl;

@RestController
@RequestMapping("/v1/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceServiceImpl invoiceServiceImpl;

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