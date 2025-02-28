package vn.demo_shipping.shipping.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.demo_shipping.shipping.domain.User;
import vn.demo_shipping.shipping.dto.request.UserRequest;

public interface UserService {
    Page<User> getAllUser(int page, int size, String sort);

    List<User> getAllUser();

    User getUserById(Long id);

    User addUser(User User);

    User updateUser(Long id, UserRequest request);

    String deleteUser(Long id);
}
