package com.vamikastore.vamika.services;

import com.vamikastore.vamika.dto.ProductDto;
import com.vamikastore.vamika.entities.Category;
import com.vamikastore.vamika.entities.CategoryType;
import com.vamikastore.vamika.entities.Product;
import com.vamikastore.vamika.exceptions.ResourceNotFoundEx;
import com.vamikastore.vamika.mapper.ProductMapper;
import com.vamikastore.vamika.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDto productDto;
    private UUID productId;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    
        // Initialize Product
        productId = UUID.randomUUID();
        product = new Product();
        product.setId(productId);
        product.setName("Laptop");
    
        // Initialize Category
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Electronics");
        product.setCategory(category);
    
        // Initialize CategoryType
        CategoryType categoryType = new CategoryType();
        categoryType.setId(UUID.randomUUID());
        categoryType.setName("Laptops");
        product.setCategoryType(categoryType);
    
        // Initialize ProductDto
        productDto = new ProductDto();
        productDto.setId(productId);
        productDto.setName("Laptop");
    }

    @Test
    public void testAddProduct() {
        when(productMapper.mapToProductEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.addProduct(productDto);

        assertNotNull(savedProduct);
        assertEquals(product.getId(), savedProduct.getId());
    }

    @Test
    public void testGetAllProducts() {
        // Mock a valid Product list
        List<Product> products = Collections.singletonList(product);
    
        // Mock repository and mapper behavior
        when(productRepository.findAll()).thenReturn(products); // Mock the repository to return the product list
        when(productMapper.getProductDtos(products)).thenReturn(Collections.singletonList(productDto)); // Mock the mapper to convert products to productDtos
    
        // Call the service method
        List<ProductDto> result = productService.getAllProducts(null, null);
    
        // Assertions
        assertNotNull(result); // Ensure the result is not null
        assertEquals(1, result.size()); // Ensure the result contains one product
        assertEquals(productDto.getId(), result.get(0).getId()); // Ensure the product ID matches
    }

    @Test
    public void testGetProductById() {
        // Mock a valid Category
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Electronics");
    
        // Mock a valid CategoryType
        CategoryType categoryType = new CategoryType();
        categoryType.setId(UUID.randomUUID());
        categoryType.setName("Laptops");
    
        // Set the Category and CategoryType in the Product
        product.setCategory(category);
        product.setCategoryType(categoryType);
    
        // Mock repository and mapper behavior
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.mapProductToDto(product)).thenReturn(productDto);
    
        // Call the service method
        ProductDto result = productService.getProductById(productId);
    
        // Assertions
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals(category.getId(), product.getCategory().getId());
        assertEquals(categoryType.getId(), product.getCategoryType().getId());
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundEx.class, () -> productService.getProductById(productId));
    }
}   