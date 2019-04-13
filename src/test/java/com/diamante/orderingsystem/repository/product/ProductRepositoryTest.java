package com.diamante.orderingsystem.repository.product;

import com.diamante.orderingsystem.TestDatabaseSetup;
import com.diamante.orderingsystem.entity.Category;
import com.diamante.orderingsystem.entity.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class ProductRepositoryTest extends TestDatabaseSetup {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Product product1;

    private Product product2;

    private Product product3;

    private Product product4;

    @Before
    public void setUp() throws Exception {
        addProductsToDatabase();
    }

    @After
    public void tearDown() throws Exception {
        productRepository.deleteAll();
    }

    @Test
    public void save_savesProductToDatabase() {
        Product savedProduct = Product.builder()
                .productName("LED Television")
                .description("A HD TV")
                .manufacturer("Samsung")
                .category(Category.ELECTRONICS)
                .price(459.99)
                .build();

        productRepository.save(savedProduct);

        Optional<Product> actualProduct = productRepository.findById(5L);

        assertThat(actualProduct.get()).isEqualToComparingFieldByField(savedProduct);
    }

    @Test
    public void findById_returnsCorrectProduct() {
        Optional<Product> actualProduct = productRepository.findById(2L);
        assertThat(actualProduct.isPresent()).isTrue();
        assertThat(actualProduct.get()).isEqualToComparingFieldByField(product2);
    }

    @Test
    public void findAll_returnsAllProductsInDatabase() {
        List<Product> expectedProductList = Arrays.asList(product1, product2, product3, product4);
        List<Product> actualProductList = (List<Product>) productRepository.findAll();
        assertThat(actualProductList).isEqualTo(expectedProductList);
    }

    @Test
    public void count_returnsNumberOfProducts() {
        assertThat(productRepository.count()).isEqualTo(4);
    }

    @Test
    public void deleteById_deletesCorrectProductFromDatabase() {
        Optional<Product> foundProduct = productRepository.findById(3L);
        assertThat(foundProduct.isPresent()).isTrue();
        productRepository.deleteById(3L);
        foundProduct = productRepository.findById(3L);
        assertThat(foundProduct.isPresent()).isFalse();
    }

    @Test
    public void findProductsByCategory_returnsListOfProductsForThatCategory() {
        List<Product> expectedProductList = Arrays.asList(product1, product4);
        List<Product> actualProductList = productRepository.findProductsByCategory(Category.ELECTRONICS);
        assertThat(actualProductList).isEqualTo(expectedProductList);
    }


    @Test
    public void findProductsByPriceLessThan50_returnsProductsWithPriceLessThan50() {
        List<Product> expectedProductList = Arrays.asList(product2, product3);
        List<Product> actualProductList = productRepository.findProductsByPriceLessThan(50.00);
        assertThat(actualProductList).isEqualTo(expectedProductList);
    }

    @Test
    public void findProductByProductName_returnsCorrectProduct() {
        Optional<Product> actualProduct = productRepository.findProductByProductName("Merlin");
        assertThat(actualProduct.isPresent()).isTrue();
        assertThat(actualProduct.get()).isEqualToComparingFieldByField(product2);
    }

    private void addProductsToDatabase() {
        productRepository.resetAllProductIds();

        product1 = Product.builder()
                .productName("Ipod")
                .description("Music player")
                .manufacturer("Apple")
                .category(Category.ELECTRONICS)
                .price(120.00)
                .build();

        product2 = Product.builder()
                .productName("Merlin")
                .description("Novel about the adventures of King Arthur and a wizard named Merlin.")
                .manufacturer("Penguin Books")
                .category(Category.BOOKS)
                .price(6.99)
                .build();

        product3 = Product.builder()
                .productName("Men's Black Watch")
                .description("A black watch")
                .manufacturer("IZOD")
                .category(Category.CLOTHING_SHOES_JEWELERY_WATCHES)
                .price(45.79)
                .build();

        product4 = Product.builder()
                .productName("Flat Screen TV")
                .description("OLED High Definition TV")
                .manufacturer("Samsung")
                .category(Category.ELECTRONICS)
                .price(599.99)
                .build();

        testEntityManager.persist(product1);
        testEntityManager.persist(product2);
        testEntityManager.persist(product3);
        testEntityManager.persist(product4);
    }
}