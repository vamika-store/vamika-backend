package com.vamikastore.vamika.controllers;

import com.vamikastore.vamika.dto.CategoryDto;
import com.vamikastore.vamika.dto.ProductDto;
import com.vamikastore.vamika.entities.Category;
import com.vamikastore.vamika.entities.Product;
import com.vamikastore.vamika.services.CategoryService;
import com.vamikastore.vamika.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CategoryController categoryController;

    @InjectMocks
    private ProductController productController;

    private Category category;
    private CategoryDto categoryDto;
    private UUID categoryId;

    private Product product;
    private ProductDto productDto;
    private UUID productId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryId = UUID.randomUUID();
        category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");
        category.setCode("ELEC");
        category.setDescription("Electronic items");

        categoryDto = new CategoryDto();
        categoryDto.setName("Electronics");
        categoryDto.setCode("ELEC");
        categoryDto.setDescription("Electronic items");

        productId = UUID.randomUUID();
        product = new Product();
        product.setId(productId);
        product.setName("Laptop");
        product.setDescription("Gaming Laptop");

        productDto = new ProductDto();
        productDto.setName("Laptop");
        productDto.setDescription("Gaming Laptop");
    }

    @Test
    public void testGetCategoryById() {
        when(categoryService.getCategory(categoryId)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.getCategoryById(categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    public void testGetAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryService.getAllCategory()).thenReturn(categories);

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
    }

    @Test
    public void testCreateCategory() {
        when(categoryService.createCategory(categoryDto)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.createCategory(categoryDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    public void testUpdateCategory() {
        when(categoryService.updateCategory(categoryDto, categoryId)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.updateCategory(categoryDto, categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    public void testDeleteCategory() {
        doNothing().when(categoryService).deleteCategory(categoryId);

        ResponseEntity<Void> response = categoryController.deleteCategory(categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryService, times(1)).deleteCategory(categoryId);
    }

    @Test
    public void testGetAllProducts() {
        List<ProductDto> products = new ArrayList<>();
        products.add(productDto);

        when(productService.getAllProducts(null, null)).thenReturn(products);

        ResponseEntity<List<ProductDto>> response = productController.getAllProducts(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    public void testGetProductByID() {
        when(productService.getProductById(productId)).thenReturn(productDto);

        ResponseEntity<ProductDto> response = productController.getProductByID(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    public void testCreateProduct() {
        when(productService.addProduct(productDto)).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(productDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    public void testUpdateProduct() {
        when(productService.updateProduct(productDto, productId)).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProduct(productDto, productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }
}