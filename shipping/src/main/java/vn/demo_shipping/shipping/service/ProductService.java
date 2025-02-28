package vn.demo_shipping.shipping.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.demo_shipping.shipping.domain.Product;
import vn.demo_shipping.shipping.dto.request.ProductRequest;

public interface ProductService {
    Page<Product> getAllProduct(int page, int size, String sort);

    List<Product> getAllProduct();

    Product getProductById(Long id);

    Product addProduct(ProductRequest request);

    Product updateProduct(Long id, ProductRequest product);

    String deleteProduct(Long id);
}
