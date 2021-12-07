package com.f197a4.registry.repository;

import java.util.Optional;

import com.f197a4.registry.domain.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    public Optional<Category> findByName(String name);
}
