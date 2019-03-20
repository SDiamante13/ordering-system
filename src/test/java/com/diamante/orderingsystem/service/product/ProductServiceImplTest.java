package com.diamante.orderingsystem.service.product;

import com.diamante.orderingsystem.entity.Category;
import com.diamante.orderingsystem.entity.Product;
import com.diamante.orderingsystem.repository.product.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService = new ProductServiceImpl(productRepository);

    Product product1 = Product.builder()
            .productId(1L)
            .productName("Ipod")
            .description("Music player")
            .manufacturer("Apple")
            .category(Category.ELECTRONICS)
            .price(120.00)
            .build();

    Product product2 = Product.builder()
            .productId(2L)
            .productName("Merlin")
            .description("Novel about the adventures of King Arthur and a wizard named Merlin.")
            .manufacturer("Penguin Books")
            .category(Category.BOOKS)
            .price(6.99)
            .build();

    @Test
    public void saveProduct_savesProductToDatabase() {
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        Product actualProduct = productService.saveProduct(product1);
        assertThat(actualProduct).isEqualToComparingFieldByField(product1);
    }

    @Test
    public void findAllProducts_returnsAllProductsInDatabase() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.findAllProducts();
        assertThat(products).isEqualTo(Arrays.asList(product1, product2));
    }

    @Test
    public void findAllProductsByCategory_returnsProductListWithSameCategory() {
        when(productRepository.findProductsByCategory(any()))
                .thenReturn(Arrays.asList(product2));

        List<Product> actualProductsList = productService.findAllProductsByCategory(Category.BOOKS);
        assertThat(actualProductsList).isEqualTo(Arrays.asList(product2));
    }

    @Test
    public void findAllProductsByPriceLessThan_returnsAllProductsLessThanPriceGiven() {
        when(productRepository.findProductsByPriceLessThan(anyDouble())).thenReturn(Arrays.asList(product1, product2));

        List<Product> actualProductsList = productService.findAllProductsByPriceLessThan(123.99);

        assertThat(actualProductsList).isEqualTo(Arrays.asList(product1, product2));
    }

    @Test
    public void findProductByName_returnsProductWithCorrectProductInfo() {
        when(productRepository.findProductByProductName(anyString())).thenReturn(Optional.of(product1));

        Product actualProduct = productService.findProductByName("Ipod");

        assertThat(actualProduct).isEqualTo(product1);
    }

    @Test
    public void findByProductId_returnsCorrectProductBasedOnProductId() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product2));

        Product actualProduct = productService.findByProductId(2L);

        assertThat(actualProduct).isEqualTo(product2);
    }

    @Test
    public void updateProduct_updatesExistingProductWithNewData() {
        Product updatedProduct = Product.builder()
                .productId(1L)
                .productName("Ipod")
                .description("Music player to jam out to!")
                .manufacturer("Apple")
                .category(Category.ELECTRONICS)
                .price(155.99)
                .build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product1));
        when(productRepository.save(any())).thenReturn(updatedProduct);

        Product actualProduct = productService.updateProduct(updatedProduct);

        assertThat(actualProduct).isEqualTo(updatedProduct);
    }

    @Test
    public void deleteProductById_removesProductBasedOnProductId() {
        doNothing().when(productRepository).deleteById(anyLong());

        productService.deleteProductById(2L);
        verify(productRepository).deleteById(eq(product2.getProductId()));
    }

    @Test
    public void deleteAllProducts_removesAllProductsFromDatabase() {
        doNothing().when(productRepository).deleteAll();

        productService.deleteAllProducts();
        verify(productRepository).deleteAll();
    }

    @Test
    public void resetAllProductIds_willCallResetAllProductIds() {
        productService.resetAllProductIds();
        verify(productRepository).resetAllProductIds();
    }
}