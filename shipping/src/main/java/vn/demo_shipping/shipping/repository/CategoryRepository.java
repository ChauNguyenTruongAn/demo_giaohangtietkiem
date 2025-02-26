package vn.demo_shipping.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.demo_shipping.shipping.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
