package vn.demo_shipping.shipping.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Invoice;
import vn.demo_shipping.shipping.domain.InvoiceProductId;
import vn.demo_shipping.shipping.domain.OrderDetail;
import vn.demo_shipping.shipping.domain.Product;
import vn.demo_shipping.shipping.domain.User;
import vn.demo_shipping.shipping.dto.request.InvoiceRequest;
import vn.demo_shipping.shipping.dto.request.OrderDetailRequest;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.NullObjectException;
import vn.demo_shipping.shipping.repository.InvoiceRepository;
import vn.demo_shipping.shipping.repository.OrderDetailRepository;
import vn.demo_shipping.shipping.repository.ProductRepository;
import vn.demo_shipping.shipping.repository.UserRepository;
import vn.demo_shipping.shipping.service.InvoiceService;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public Invoice addInvoice(InvoiceRequest request) {
        if (request == null)
            throw new NullObjectException("Invoice is null!!");

        // get user
        User user = userRepository.findById(request.getUser_id()).orElseThrow(
                () -> new NotFoundException("User not found"));

        // order detail
        // OrderDetail orderDetail =
        // orderDetailRepository.findById(request.getOrder_detail_id()).orElseThrow(
        // () -> new NotFoundException("Invalid Order Detail"));

        // get old invoice
        Invoice existingInvoice = new Invoice();
        // set new value
        existingInvoice.setTotal(request.getTotal());
        existingInvoice.setUser(user);
        // existingInvoice.addOrderDetail(orderDetail);

        return invoiceRepository.save(existingInvoice);
    }

    @Override
    public String deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id))
            throw new NullObjectException("Invoice does not exists!!");
        invoiceRepository.deleteById(id);
        return "Delete invoice success";
    }

    @Override
    public Page<Invoice> getAllInvoice(int page, int size, String request) {
        Sort sort;
        if (request.compareTo("asc") == 0) {
            sort = Sort.by(Sort.Order.asc("id"));
        } else if (request.compareTo("desc") == 0) {
            sort = Sort.by(Sort.Order.desc("id"));
        } else {
            sort = Sort.by(Sort.Order.asc("id"));
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return invoiceRepository.findAll(pageable);

    }

    @Override
    public List<Invoice> getAllInvoice() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Invoice not found"));
    }

    @Override
    public Invoice updateInvoice(Long id, InvoiceRequest request) {

        // get user
        User user = userRepository.findById(request.getUser_id()).orElseThrow(
                () -> new NotFoundException("User not found"));

        // order detail
        OrderDetail orderDetail = orderDetailRepository.findById(request.getOrder_detail_id()).orElseThrow(
                () -> new NotFoundException("Invalid Order Detail"));

        // get old invoice
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice with ID " + id + " not found"));

        // set new value
        existingInvoice.setTotal(request.getTotal());
        existingInvoice.setUser(user);
        existingInvoice.addOrderDetail(orderDetail);

        // update
        return invoiceRepository.save(existingInvoice);
    }

    @Override
    public Invoice addProduct(Long id, OrderDetailRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Invalid product");
        }

        Product product = productRepository.findById(request.getProduct_id())
                .orElseThrow(() -> new NotFoundException("Not found Product" + request.getProduct_id()));

        Invoice invoice = invoiceRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Not found Invoice" + id));

        User user = userRepository.findById(invoice.getUser().getId()).orElseThrow(
                () -> new NotFoundException("Not found User " + id));

        InvoiceProductId invoiceProductId = new InvoiceProductId(id, request.getProduct_id());

        OrderDetail orderDetail = OrderDetail.builder()
                .product(product)
                .invoice(invoice)
                .id(invoiceProductId)
                .quantity(request.getQuantity())
                .tax(request.getTax())
                .build();

        invoice.setUser(user);

        invoice.addOrderDetail(orderDetail);

        double productPriceWithTax = product.getPrice() * (1 + orderDetail.getTax() / 100);

        double totalPrice = productPriceWithTax * orderDetail.getQuantity();

        invoice.setTotal(invoice.getTotal() + totalPrice);

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice removeProduct(Long id, InvoiceProductId orderDetailId) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Not found invoice"));

        Product product = productRepository.findById(orderDetailId.getProduct_id())
                .orElseThrow(() -> new NotFoundException("Not found Product" + orderDetailId.getProduct_id()));

        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(
                () -> new NotFoundException("Not found OrderDetail"));

        double productPriceWithTax = product.getPrice() * (1 + orderDetail.getTax() / 100);

        double totalPriceToRemove = productPriceWithTax * orderDetail.getQuantity();

        invoice.setTotal(invoice.getTotal() - totalPriceToRemove);

        invoice.removeOrderDetail(orderDetail);

        return invoiceRepository.save(invoice);
    }

}
