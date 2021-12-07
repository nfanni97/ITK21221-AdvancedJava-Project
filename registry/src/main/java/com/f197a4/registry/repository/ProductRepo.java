package com.f197a4.registry.repository;

import com.f197a4.registry.domain.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Long> {
    
}
