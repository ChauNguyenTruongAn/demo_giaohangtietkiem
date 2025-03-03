package vn.demo_shipping.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.demo_shipping.shipping.domain.GHTKOrder;

public interface GHTKOrderRepository extends JpaRepository<GHTKOrder, Long> {

}
