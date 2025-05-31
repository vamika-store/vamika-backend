package com.vamikastore.vamika.services;

import com.vamikastore.vamika.dto.CategoryDto;
import com.vamikastore.vamika.dto.CategoryTypeDto;
import com.vamikastore.vamika.entities.Category;
import com.vamikastore.vamika.entities.CategoryType;
import com.vamikastore.vamika.exceptions.ResourceNotFoundEx;
import com.vamikastore.vamika.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCategory_shouldReturnCategory() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategory(id);

        assertEquals(category, result);
    }

    @Test
    void getCategory_shouldReturnNullIfNotFound() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        Category result = categoryService.getCategory(id);

        assertNull(result);
    }

    @Test
    void createCategory_shouldSaveCategory() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Test");
        dto.setCode("TST");
        dto.setDescription("desc");
        dto.setCategoryTypeList(new ArrayList<>());
        Category category = Category.builder().name("Test").code("TST").description("desc").build();
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.createCategory(dto);

        assertEquals(category.getName(), result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void getAllCategory_shouldReturnAll() {
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategory();

        assertEquals(categories, result);
    }

    @Test
    void updateCategory_shouldUpdateAndReturnCategory() {
        UUID id = UUID.randomUUID();
        CategoryDto dto = new CategoryDto();
        dto.setName("Updated");
        dto.setCode("UPD");
        dto.setDescription("desc");
        dto.setCategoryTypeList(new ArrayList<>());
        Category category = Category.builder().name("Old").code("OLD").description("old").categoryTypes(new ArrayList<>()).build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.updateCategory(dto, id);

        assertEquals(category, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void updateCategory_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        CategoryDto dto = new CategoryDto();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundEx.class, () -> categoryService.updateCategory(dto, id));
    }

    @Test
    void deleteCategory_shouldDelete() {
        UUID id = UUID.randomUUID();
        doNothing().when(categoryRepository).deleteById(id);

        categoryService.deleteCategory(id);

        verify(categoryRepository).deleteById(id);
    }
}