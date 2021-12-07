package com.f197a4.registry.controller;

import java.util.List;

import javax.naming.NameNotFoundException;

import com.f197a4.registry.domain.Category;
import com.f197a4.registry.exception.CategoryNotFoundException;
import com.f197a4.registry.repository.CategoryRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Category addNewCategory(@RequestBody Category newCategory) {
        return categoryRepo.save(newCategory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Category modifyCategory(@RequestBody Category modifiedCategory, @PathVariable Long id) {
        Category toUpdate = categoryRepo.getById(id);
        toUpdate.setName(modifiedCategory.getName());
        return categoryRepo.save(toUpdate);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Category getCategory(@PathVariable Long id) {
        return categoryRepo.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
