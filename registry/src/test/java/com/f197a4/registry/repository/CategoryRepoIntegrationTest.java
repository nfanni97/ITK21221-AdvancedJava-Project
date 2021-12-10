package com.f197a4.registry.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.f197a4.registry.RegistryApplication;
import com.f197a4.registry.domain.Category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    classes=RegistryApplication.class
)
public class CategoryRepoIntegrationTest {

    @Autowired
    private CategoryRepo categoryRepo;

    @Test
    void testFindByName() {
        Category retrieved = categoryRepo.findByName("decoration").get();
        assertEquals("decoration", retrieved.getName());
        assertEquals(2L, retrieved.getId());
    }
}
