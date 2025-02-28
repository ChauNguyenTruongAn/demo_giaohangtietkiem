package vn.demo_shipping.shipping.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.demo_shipping.shipping.domain.Invoice;
import vn.demo_shipping.shipping.dto.request.InvoiceRequest;

public interface InvoiceService {
    Page<Invoice> getAllInvoice(int page, int size, String sort);

    List<Invoice> getAllInvoice();

    Invoice getInvoiceById(Long id);

    Invoice addInvoice(Invoice invoice);

    Invoice updateInvoice(Long id, InvoiceRequest request);

    String deleteInvoice(Long id);
}
