package vn.demo_shipping.shipping.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.demo_shipping.shipping.domain.OrderDetail;
import vn.demo_shipping.shipping.dto.request.OrderDetailRequest;

public interface OrderDetailService {
    Page<OrderDetail> getAllOrderDetail(int page, int size, String sort);

    List<OrderDetail> getAllOrderDetail();

    OrderDetail getOrderDetailById(Long id);

    OrderDetail addOrderDetail(OrderDetail orderDetail);

    OrderDetail updateOrderDetail(Long id, OrderDetailRequest request);

    String deleteOrderDetail(Long id);
}
