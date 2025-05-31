package com.vamikastore.vamika.services;

import com.vamikastore.vamika.dto.CategoryDto;
import com.vamikastore.vamika.entities.Category;
import com.vamikastore.vamika.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private UUID categoryId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryId = UUID.randomUUID();
        category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        categoryDto.setName("Electronics");
    }

    @Test
    public void testGetCategory() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getCategory(categoryId);

        assertNotNull(foundCategory);
        assertEquals(category.getId(), foundCategory.getId());
    }

    @Test
    public void testCreateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category savedCategory = categoryService.createCategory(categoryDto);

        assertNotNull(savedCategory);
        assertEquals(category.getId(), savedCategory.getId());
    }
}