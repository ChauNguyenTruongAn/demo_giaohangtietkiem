package vn.demo_shipping.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.demo_shipping.shipping.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
