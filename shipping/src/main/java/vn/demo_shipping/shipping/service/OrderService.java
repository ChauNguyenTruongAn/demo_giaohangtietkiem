package vn.demo_shipping.shipping.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.demo_shipping.shipping.domain.Order;
import vn.demo_shipping.shipping.dto.request.OrderRequest;

public interface OrderService {
    Page<Order> getAllOrder(int page, int size, String sort);

    List<Order> getAllOrder();

    Order getOrderById(Long id);

    Order addOrder(OrderRequest Order);

    Order updateOrder(Long id, OrderRequest request);

    String deleteOrder(Long id);
}
