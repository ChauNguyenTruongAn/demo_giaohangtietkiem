package vn.demo_shipping.shipping.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Category;
import vn.demo_shipping.shipping.dto.request.CategoryRequest;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.repository.CategoryRepository;
import vn.demo_shipping.shipping.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Page<Category> getAllCategory(int page, int size, String request) {
        Sort sort;
        if (request.compareTo("asc") == 0)
            sort = Sort.by(Sort.Order.asc("name"));
        else
            sort = Sort.by(Sort.Order.desc("name"));
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
    public Category addCategory(Category category) {
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

}
