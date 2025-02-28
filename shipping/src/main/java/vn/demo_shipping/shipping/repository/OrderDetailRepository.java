package vn.demo_shipping.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.demo_shipping.shipping.domain.InvoiceProductId;
import vn.demo_shipping.shipping.domain.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, InvoiceProductId> {

}