package vn.demo_shipping.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.demo_shipping.shipping.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
