package com.f197a4.registry.repository;

import com.f197a4.registry.domain.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    
}
