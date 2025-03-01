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
import vn.demo_shipping.shipping.dto.request.CategoryRequest;
import vn.demo_shipping.shipping.dto.request.ProductRequest;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.repository.CategoryRepository;
import vn.demo_shipping.shipping.repository.ProductRepository;
import vn.demo_shipping.shipping.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<Category> getAllCategory(int page, int size, String request) {
        Sort sort;
        if (request.compareTo("asc") == 0)
            sort = Sort.by(Sort.Order.asc("id"));
        else
            sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, size, sort);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Id do not exists in database"));
    }

    @Override
    public Category addCategory(CategoryRequest request) {

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        try {
            categoryRepository.deleteById(id);
            return "Delete user success: " + id;
        } catch (Exception e) {
            return "Not found " + id + " in database";
        }
    }

    @Override
    public Category updateCategory(Long id, CategoryRequest category) {
        try {
            Category currentCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Can't find category in database"));
            currentCategory.setName(category.getName());
            currentCategory.setDescription(category.getDescription());
            return categoryRepository.save(currentCategory);
        } catch (NotFoundException e) {
            return null;
        }
    }

    @Override
    public Category addProduct(Long id, ProductRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Invalid request");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .weight(request.getWeight())
                .image(request.getImage())
                .inventory(request.getInventory())
                .category(category)
                .build();

        category.addProduct(product);

        categoryRepository.save(category);

        return category;
    }

    @Override
    public Category removeCategory(Long id, Long product_id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        category.removeProduct(product);

        categoryRepository.save(category);

        return category;
    }
}
