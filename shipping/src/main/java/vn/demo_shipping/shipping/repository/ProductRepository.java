package vn.demo_shipping.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.demo_shipping.shipping.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
