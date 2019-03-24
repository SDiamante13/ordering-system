package com.diamante.orderingsystem.integration.product;

import com.diamante.orderingsystem.OrderingSystemApplication;
import com.diamante.orderingsystem.TestDatabaseSetup;
import com.diamante.orderingsystem.entity.Category;
import com.diamante.orderingsystem.entity.Product;
import com.diamante.orderingsystem.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.diamante.orderingsystem.entity.Category.ELECTRONICS;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderingSystemApplication.class)
@ActiveProfiles("default")
public class ProductControllerIntegrationTest extends TestDatabaseSetup {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ProductService productService;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        addProductsToDatabase();
    }

    @After
    public void tearDown() throws Exception {
        productService.deleteAllProducts();
    }

    @AfterClass
    public static void closeTestContainer() throws Exception {
        postgreSQLContainer.close();
    }





    private void addProductsToDatabase() {
        productService.resetAllProductIds();

        Product product1 = Product.builder()
                .productName("Ipod")
                .description("Music player")
                .manufacturer("Apple")
                .category(ELECTRONICS)
                .price(120.00)
                .build();

        Product product2 = Product.builder()
                .productName("Merlin")
                .description("Novel about the adventures of King Arthur and a wizard named Merlin.")
                .manufacturer("Penguin Books")
                .category(Category.BOOKS)
                .price(6.99)
                .build();

        Product product3 = Product.builder()
                .productName("Men's Black Watch")
                .description("A black watch")
                .manufacturer("IZOD")
                .category(Category.CLOTHING_SHOES_JEWELERY_WATCHES)
                .price(45.79)
                .build();

        Product product4 = Product.builder()
                .productName("Flat Screen TV")
                .description("OLED High Definition TV")
                .manufacturer("Samsung")
                .category(ELECTRONICS)
                .price(599.99)
                .build();

        productService.saveProduct(product1);
        productService.saveProduct(product2);
        productService.saveProduct(product3);
        productService.saveProduct(product4);
    }
}