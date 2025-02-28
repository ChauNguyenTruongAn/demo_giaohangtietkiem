package vn.demo_shipping.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.demo_shipping.shipping.domain.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

}
