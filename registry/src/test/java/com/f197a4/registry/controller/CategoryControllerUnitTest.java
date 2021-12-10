package com.f197a4.registry.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.util.Optional;

import com.f197a4.registry.domain.Category;
import com.f197a4.registry.exception.CategoryNotFoundException;
import com.f197a4.registry.repository.CategoryRepo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerUnitTest {

    @MockBean
    private static CategoryRepo categoryRepo;

    @Autowired
    @InjectMocks
    private CategoryController categoryController;

    private static Category two = new Category(2L,"kitchen");
    private static Category modifiedCategory = new Category("kitchenware");

    @BeforeAll
    static void initializeMockRepo() {
        categoryRepo = mock(CategoryRepo.class);
        when(categoryRepo.findById(2L)).thenReturn(Optional.of(two));
        when(categoryRepo.findById(1L)).thenReturn(Optional.empty());
        when(categoryRepo.save(any(Category.class))).then(returnsFirstArg());
    }

    @Test
    void testModifyCategorySuccess() {
        Category returnedCategory = categoryController.modifyCategory(modifiedCategory, 2L);
        Category expectedCategory = new Category(2L,"kitchenware");
        assertEquals(expectedCategory, returnedCategory);
    }

    @Test
    void testModifyCategoryNotFound() {
        assertThrows(CategoryNotFoundException.class, () -> categoryController.modifyCategory(modifiedCategory, 1L));
    }
}
