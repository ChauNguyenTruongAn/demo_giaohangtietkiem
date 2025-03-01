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
import vn.demo_shipping.shipping.dto.request.OrderDetailRequest;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.NullObjectException;
import vn.demo_shipping.shipping.repository.InvoiceRepository;
import vn.demo_shipping.shipping.repository.OrderDetailRepository;
import vn.demo_shipping.shipping.repository.ProductRepository;
import vn.demo_shipping.shipping.service.OrderDetailService;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public OrderDetail addOrderDetail(OrderDetail orderDetail) {
        if (orderDetail == null)
            throw new NullObjectException("OrderDetail is null!!");
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public String deleteOrderDetail(InvoiceProductId id) {
        if (!orderDetailRepository.existsById(id))
            throw new NullObjectException("OrderDetail is null!!");
        orderDetailRepository.deleteById(id);
        return "Delete orderDetail success";
    }

    @Override
    public Page<OrderDetail> getAllOrderDetail(int page, int size, String request) {
        Sort sort;
        if (request.compareTo("asc") == 0) {
            sort = Sort.by(Sort.Order.asc("id"));
        } else if (request.compareTo("desc") == 0) {
            sort = Sort.by(Sort.Order.desc("id"));
        } else {
            sort = Sort.by(Sort.Order.asc("id"));
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderDetailRepository.findAll(pageable);

    }

    @Override
    public List<OrderDetail> getAllOrderDetail() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetail getOrderDetailById(InvoiceProductId id) {
        return orderDetailRepository.findById(id).orElseThrow(
                () -> new NotFoundException("OrderDetail not found"));
    }

    @Override
    public OrderDetail updateOrderDetail(InvoiceProductId id, OrderDetailRequest request) {

        // get products
        Product product = productRepository.findById(request.getProduct_id()).orElseThrow(
                () -> new NotFoundException("Invalid invoice"));

        Invoice invoice = invoiceRepository.findById(request.getInvoice_id()).orElseThrow(
                () -> new NotFoundException("Invalid invoice"));

        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("OrderDetail with ID " + id + " not found"));

        existingOrderDetail.setQuantity(request.getQuantity());
        existingOrderDetail.setTax(request.getTax());
        existingOrderDetail.setInvoice(invoice);
        existingOrderDetail.setProduct(product);
        return orderDetailRepository.save(existingOrderDetail);
    }

}
