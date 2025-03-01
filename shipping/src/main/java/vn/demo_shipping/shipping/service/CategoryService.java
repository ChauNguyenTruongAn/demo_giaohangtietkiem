package vn.demo_shipping.shipping.service;

import java.util.List;

import org.springframework.data.domain.Page;
import vn.demo_shipping.shipping.domain.Category;
import vn.demo_shipping.shipping.dto.request.CategoryRequest;
import vn.demo_shipping.shipping.dto.request.ProductRequest;

public interface CategoryService {
    Page<Category> getAllCategory(int page, int size, String sort);

    List<Category> getAllCategory();

    Category getCategoryById(Long id);

    Category addCategory(CategoryRequest request);

    Category updateCategory(Long id, CategoryRequest category);

    String deleteCategory(Long id);

    Category addProduct(Long id, ProductRequest request);   

    Category removeCategory(Long id, Long product_id);
}
