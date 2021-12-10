package com.f197a4.registry.controller;

import java.util.List;

import com.f197a4.registry.domain.Category;
import com.f197a4.registry.exception.CategoryNotFoundException;
import com.f197a4.registry.repository.CategoryRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content/categories")
public class CategoryController {

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Category> getCategories() {
        logger.info("Returning all categories: {}.",categoryRepo.findAll());
        return categoryRepo.findAll();
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Category addNewCategory(@RequestBody Category newCategory) {
        logger.info("Creating new category: {}.",newCategory);
        return categoryRepo.save(newCategory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Category modifyCategory(@RequestBody Category modifiedCategory, @PathVariable Long id) {
        Category toUpdate = categoryRepo.findById(id).orElseThrow(() -> {
            logger.error("Category with id {} not found.",id);
            return new CategoryNotFoundException(id);
        });
        logger.info("Modifying category with id {}. Previous name: {}. New name: {}.",id,toUpdate.getName(),modifiedCategory.getName());
        toUpdate.setName(modifiedCategory.getName());
        logger.info("Category {} modified.",id);
        return categoryRepo.save(toUpdate);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Category getCategory(@PathVariable Long id) {
        logger.info("Getting category with id {}.",id);
        Category cat = categoryRepo.findById(id).orElseThrow(() -> {
            logger.error("Category with id {} not found.",id);
            return new CategoryNotFoundException(id);
        });
        logger.info("Category found: {}.",cat);
        return cat;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        logger.info("Deleting category with id {}.",id);
        if(!categoryRepo.existsById(id)) {
            logger.error("Category {} not found.",id);
            throw new CategoryNotFoundException(id);
        }
        categoryRepo.deleteById(id);
        logger.info("Category {} deleted.",id);
    }
}
