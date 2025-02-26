package vn.demo_shipping.shipping.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Category;
import vn.demo_shipping.shipping.domain.Product;
import vn.demo_shipping.shipping.dto.request.ProductRequest;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.repository.CategoryRepository;
import vn.demo_shipping.shipping.repository.ProductRepository;
import vn.demo_shipping.shipping.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public String deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
            return "Delete product success: " + id;
        } catch (Exception e) {
            throw new NotFoundException("Product does not exists in database");
        }
    }

    @Override
    public Page<Product> getAllProduct(int page, int size, String request) {
        Sort sort;
        if (request.compareTo("asc") == 0)
            sort = Sort.by(Sort.Order.asc("name"));
        else
            sort = Sort.by(Sort.Order.desc("name"));
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product does not exists in database"));
    }

    @Override
    public Product updateProduct(Long id, ProductRequest product) {
        try {

            Category category = categoryRepository.findById(product.getCategory_id()).orElseThrow(
                    () -> new IllegalArgumentException("Invalid category"));

            Product currentProduct = productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Can't find product in database"));
            currentProduct.setName(product.getName());
            currentProduct.setPrice(product.getPrice());
            currentProduct.setWeight(product.getWeight());
            currentProduct.setImage(product.getImage());
            currentProduct.setInventory(product.getInventory());
            currentProduct.setCategory(category);
            return productRepository.save(currentProduct);
        } catch (NotFoundException e) {
            throw new NotFoundException("Can't find product in database");
        }
    }

}
