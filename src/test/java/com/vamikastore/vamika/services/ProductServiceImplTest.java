package com.vamikastore.vamika.services;

import com.vamikastore.vamika.dto.ProductDto;
import com.vamikastore.vamika.entities.Product;
import com.vamikastore.vamika.exceptions.ResourceNotFoundEx;
import com.vamikastore.vamika.mapper.ProductMapper;
import com.vamikastore.vamika.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProduct_shouldSaveProduct() {
        ProductDto dto = new ProductDto();
        Product product = new Product();
        when(productMapper.mapToProductEntity(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.addProduct(dto);

        assertEquals(product, result);
        verify(productRepository).save(product);
    }

    @Test
    void getAllProducts_shouldReturnProductDtos() {
        UUID categoryId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();
        List<Product> products = Arrays.asList(new Product(), new Product());
        List<ProductDto> productDtos = Arrays.asList(new ProductDto(), new ProductDto());

        when(productRepository.findAll(any(Specification.class))).thenReturn(products);
        when(productMapper.getProductDtos(products)).thenReturn(productDtos);

        List<ProductDto> result = productService.getAllProducts(categoryId, typeId);

        assertEquals(productDtos, result);
    }

    @Test
    void getProductBySlug_shouldReturnProductDto() {
        String slug = "test-slug";
        Product product = mock(Product.class);
        ProductDto dto = new ProductDto();
        when(productRepository.findBySlug(slug)).thenReturn(product);
        when(productMapper.mapProductToDto(product)).thenReturn(dto);
        when(product.getCategory()).thenReturn(mock(com.vamikastore.vamika.entities.Category.class));
        when(product.getCategoryType()).thenReturn(mock(com.vamikastore.vamika.entities.CategoryType.class));
        when(product.getProductVariants()).thenReturn(Collections.emptyList());
        when(product.getResources()).thenReturn(Collections.emptyList());

        ProductDto result = productService.getProductBySlug(slug);

        assertEquals(dto, result);
    }

    @Test
    void getProductBySlug_shouldThrowIfNotFound() {
        when(productRepository.findBySlug("notfound")).thenReturn(null);
        assertThrows(ResourceNotFoundEx.class, () -> productService.getProductBySlug("notfound"));
    }

    @Test
    void getProductById_shouldReturnProductDto() {
        UUID id = UUID.randomUUID();
        Product product = mock(Product.class);
        ProductDto dto = new ProductDto();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productMapper.mapProductToDto(product)).thenReturn(dto);
        when(product.getCategory()).thenReturn(mock(com.vamikastore.vamika.entities.Category.class));
        when(product.getCategoryType()).thenReturn(mock(com.vamikastore.vamika.entities.CategoryType.class));
        when(product.getProductVariants()).thenReturn(Collections.emptyList());
        when(product.getResources()).thenReturn(Collections.emptyList());

        ProductDto result = productService.getProductById(id);

        assertEquals(dto, result);
    }

    @Test
    void getProductById_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundEx.class, () -> productService.getProductById(id));
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {
        UUID id = UUID.randomUUID();
        ProductDto dto = new ProductDto();
        Product product = new Product();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productMapper.mapToProductEntity(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProduct(dto, id);

        assertEquals(product, result);
        verify(productRepository).save(product);
    }

    @Test
    void fetchProductById_shouldReturnProduct() throws Exception {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product result = productService.fetchProductById(id);

        assertEquals(product, result);
    }

    @Test
    void fetchProductById_shouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> productService.fetchProductById(id));
    }
}