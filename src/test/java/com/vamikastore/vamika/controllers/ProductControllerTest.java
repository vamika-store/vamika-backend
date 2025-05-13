package com.vamikastore.vamika.controllers;

import com.vamikastore.vamika.dto.ProductDto;
import com.vamikastore.vamika.entities.Product;
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
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private ProductDto productDto;
    private UUID productId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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