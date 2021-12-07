package com.f197a4.registry.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.f197a4.registry.domain.Category;
import com.f197a4.registry.domain.Product;
import com.f197a4.registry.exception.ProductNotFoundException;
import com.f197a4.registry.payload.request.ProductRequest;
import com.f197a4.registry.repository.CategoryRepo;
import com.f197a4.registry.repository.ProductRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/content/products")
public class ProductController {
    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Product> getCategories() {
        return productRepo.findAll();
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Product addNewProduct(@RequestBody ProductRequest productRequest) {
        logger.info("Request received: name {}, price {}, categories {}",productRequest.getName(),productRequest.getPriceHuf(),productRequest.getCategories());
        Set<Category> categories = checkAndSaveCategories(productRequest.getCategories());
        logger.debug("categories: {}",categories.toString());
        return productRepo.save(new Product(
            productRequest.getName(),
            productRequest.getPriceHuf(),
            categories
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Product modifyProduct(@RequestBody ProductRequest modifiedProduct, @PathVariable Long id) {
        Product toUpdate = productRepo.getById(id);
        Set<Category> categories = checkAndSaveCategories(modifiedProduct.getCategories());
        toUpdate.setCategories(categories);
        toUpdate.setName(modifiedProduct.getName());
        toUpdate.setPriceHuf(modifiedProduct.getPriceHuf());
        return productRepo.save(toUpdate);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Product getProduct(@PathVariable Long id) {
        return productRepo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    private Set<Category> checkAndSaveCategories(Set<String> categoryStrs) {
        // check and save categories
        Set<Category> categories = categoryStrs.stream()
            .map(categoryStr -> {
                logger.debug("working on category {}", categoryStr);
                if(categoryRepo.findByName(categoryStr).isEmpty()) {
                    logger.debug("Category {} is not in repo, saving now",categoryStr);
                    return categoryRepo.save(new Category(categoryStr));
                } else {
                    logger.debug("Category {} already in repo, loading",categoryStr);
                    return categoryRepo.findByName(categoryStr).get();
                }
            }).collect(Collectors.toSet());
        return categories;
    }
}
