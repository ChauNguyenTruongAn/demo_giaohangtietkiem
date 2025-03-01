package vn.demo_shipping.shipping.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.demo_shipping.shipping.domain.Invoice;
import vn.demo_shipping.shipping.domain.InvoiceProductId;
import vn.demo_shipping.shipping.domain.OrderDetail;
import vn.demo_shipping.shipping.dto.request.InvoiceRequest;
import vn.demo_shipping.shipping.dto.request.OrderDetailRequest;
import vn.demo_shipping.shipping.dto.request.ProductRequest;

public interface InvoiceService {
    Page<Invoice> getAllInvoice(int page, int size, String sort);

    List<Invoice> getAllInvoice();

    Invoice getInvoiceById(Long id);

    Invoice addInvoice(InvoiceRequest request);

    Invoice updateInvoice(Long id, InvoiceRequest request);

    String deleteInvoice(Long id);

    Invoice addProduct(Long id, OrderDetailRequest request);

    Invoice removeProduct(Long id, InvoiceProductId orderDetailId);
}
