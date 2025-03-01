package vn.demo_shipping.shipping.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Invoice;
import vn.demo_shipping.shipping.domain.Order;
import vn.demo_shipping.shipping.domain.Warehouse;
import vn.demo_shipping.shipping.dto.request.OrderRequest;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.NullObjectException;
import vn.demo_shipping.shipping.repository.InvoiceRepository;
import vn.demo_shipping.shipping.repository.OrderRepository;
import vn.demo_shipping.shipping.repository.WarehouseRepository;
import vn.demo_shipping.shipping.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public Order addOrder(OrderRequest request) {
        if (request == null)
            throw new NullObjectException("Order is null!!");

        Invoice invoice = invoiceRepository.findById(request.getInvoice_id()).orElseThrow(
                () -> new NotFoundException("Invoice not found"));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouse_id()).orElseThrow(
                () -> new NotFoundException("Invoice not found"));

        Order order = new Order();

        order.setNote(request.getNote());
        order.setReturn_name(request.getReturn_name());
        order.setReturn_address(request.getReturn_address());
        order.setReturn_province(request.getReturn_province());
        order.setReturn_district(request.getReturn_district());
        order.setReturn_ward(request.getReturn_ward());
        order.setReturn_street(request.getReturn_street());
        order.setReturn_tel(request.getReturn_tel());
        order.setReturn_email(request.getReturn_email());
        order.setIs_freeship(request.getIs_freeship());
        order.setWeight_option(request.getWeight_option());
        order.setTotal_weight(request.getTotal_weight());
        order.setValue(request.getValue());
        // invoice
        order.addInvoice(invoice);
        // warehouse
        order.addWarehouse(warehouse);
        return orderRepository.save(order);
    }

    @Override
    public String deleteOrder(Long id) {
        // if (!orderRepository.existsById(id))
        // throw new NullObjectException("Order is null!!");

        Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Not found order"));

        order.setInvoice(null);
        order.setWarehouses(null);

        orderRepository.save(order);

        orderRepository.deleteById(id);
        return "Delete order success";
    }

    @Override
    public Page<Order> getAllOrder(int page, int size, String request) {
        Sort sort;
        if (request.compareTo("asc") == 0) {
            sort = Sort.by(Sort.Order.asc("id"));
        } else if (request.compareTo("desc") == 0) {
            sort = Sort.by(Sort.Order.desc("id"));
        } else {
            sort = Sort.by(Sort.Order.asc("id"));
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findAll(pageable);

    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Order not found"));
    }

    @Override
    public Order updateOrder(Long id, OrderRequest request) {

        Invoice invoice = invoiceRepository.findById(request.getInvoice_id()).orElseThrow(
                () -> new NotFoundException("Not found order"));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouse_id()).orElseThrow(
                () -> new NotFoundException("Not found warehouse"));

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with ID " + id + " not found"));

        existingOrder.setNote(request.getNote());
        existingOrder.setReturn_name(request.getReturn_name());
        existingOrder.setReturn_address(request.getReturn_address());
        existingOrder.setReturn_province(request.getReturn_province());
        existingOrder.setReturn_district(request.getReturn_district());
        existingOrder.setReturn_ward(request.getReturn_ward());
        existingOrder.setReturn_street(request.getReturn_street());
        existingOrder.setReturn_tel(request.getReturn_tel());
        existingOrder.setReturn_email(request.getReturn_email());
        existingOrder.setIs_freeship(request.getIs_freeship());
        existingOrder.setWeight_option(request.getWeight_option());
        existingOrder.setTotal_weight(request.getTotal_weight());
        existingOrder.setValue(request.getValue());
        existingOrder.setInvoice(invoice);
        existingOrder.setWarehouses(warehouse);
        return orderRepository.save(existingOrder);
    }
}
