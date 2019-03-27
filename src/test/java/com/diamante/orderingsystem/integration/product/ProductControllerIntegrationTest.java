package com.diamante.orderingsystem.integration.product;

import com.diamante.orderingsystem.OrderingSystemApplication;
import com.diamante.orderingsystem.TestDatabaseSetup;
import com.diamante.orderingsystem.entity.Category;
import com.diamante.orderingsystem.entity.Product;
import com.diamante.orderingsystem.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.diamante.orderingsystem.entity.Category.ELECTRONICS;
import static com.diamante.orderingsystem.entity.Category.HOME_LIVING;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    public void getAllProducts_returnsAllProducts() throws Exception {
        mockMvc.perform(get("/product/list"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Ipod"))
                .andExpect(jsonPath("$[0].manufacturer").value("Apple"))
                .andExpect(jsonPath("$[1].productName").value("Merlin"))
                .andExpect(jsonPath("$[1].manufacturer").value("Penguin Books"))
                .andExpect(jsonPath("$[2].productName").value("Men's Black Watch"))
                .andExpect(jsonPath("$[2].manufacturer").value("IZOD"))
                .andExpect(jsonPath("$[3].productName").value("Flat Screen TV"))
                .andExpect(jsonPath("$[3].manufacturer").value("Samsung"));
    }

    @Test
    public void getAllProducts_withParameterCategoryBOOKS_returnsAllBooks() throws Exception {
        mockMvc.perform(get("/product/list?cat=BOOKS"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productName").value("Merlin"))
                .andExpect(jsonPath("$[0].manufacturer").value("Penguin Books"));
    }

    @Test
    public void getAllProducts_withParameterPrice125_returnsAllProductsLessThanPrice125() throws Exception {
        mockMvc.perform(get("/product/list?price=125"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].productName").value("Ipod"))
                .andExpect(jsonPath("$[0].manufacturer").value("Apple"))
                .andExpect(jsonPath("$[1].productName").value("Merlin"))
                .andExpect(jsonPath("$[1].manufacturer").value("Penguin Books"))
                .andExpect(jsonPath("$[2].productName").value("Men's Black Watch"))
                .andExpect(jsonPath("$[2].manufacturer").value("IZOD"));
    }

    @Test
    public void getProduct_withParameterId_returnsCorrectProduct() throws Exception {
        mockMvc.perform(get("/product?id=1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Ipod"))
                .andExpect(jsonPath("$.manufacturer").value("Apple"));
    }

    @Test
    public void getProduct_withParameterNameWithMultipleWords_returnsCorrectProduct() throws Exception {
        mockMvc.perform(get("/product?name=Men's%Black%Watch"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("A black watch"))
                .andExpect(jsonPath("$.manufacturer").value("IZOD"));
    }

    @Test
    public void getProduct_withParameterName_returnsCorrectProduct() throws Exception {
        mockMvc.perform(get("/product?name=Merlin"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Novel about the adventures of King Arthur and a wizard named Merlin."))
                .andExpect(jsonPath("$.manufacturer").value("Penguin Books"));
    }

    @Test
    public void getProductByIdOrName_withUnknownParameter_throwsNoSuchMethodException() throws Exception {
        mockMvc.perform(get("/product/?some=thing"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unable to process request."));
    }

    @Test
    public void createProduct_createsNewProductInDatabaseGivenValidResponseBody() throws Exception {
        Product product5 = Product.builder()
                .productName("Living Room Chair")
                .description("A single chair")
                .manufacturer("Design Factory")
                .category(HOME_LIVING)
                .price(299.99)
                .build();

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product5))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void createProduct_returnsProductIdAutoGeneratedExceptionMessage_whenProductHasAnIdBeforeBeingSaved() throws Exception {
        Product productWithForcedId = Product.builder()
                .productId(7L)
                .productName("Living Room Chair")
                .description("A single chair")
                .manufacturer("Design Factory")
                .category(HOME_LIVING)
                .price(299.99)
                .build();

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productWithForcedId))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", is("Product id is auto generated. Please remove this field from your request.")));

    }

    @Test
    public void createProduct_returnsValidationMessage() throws Exception {
        Product productWithLongDesc= Product.builder()
                .productName("Living Room Chair")
                .description("The best chair in the world. I dare you to find another. It will knock our socks off and then you can sit down in it. Simply Amazing!")
                .manufacturer("Design Factory")
                .category(HOME_LIVING)
                .price(299.99)
                .build();

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productWithLongDesc))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("The description must be no longer than 40 characters")));

    }

    @Test
    public void updateProduct_returnsValidationMessage_whenMissingManufacturer() throws Exception {
        Product updatedProduct = Product.builder()
                .productId(3L)
                .productName("Living Room Chair")
                .description("The best chair in the world.")
                .category(HOME_LIVING)
                .price(299.99)
                .build();

        mockMvc.perform(put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field manufacturer with value null does not meet requirements. Manufacturer is a required field")));
    }

    @Test
    public void updateProduct_updatesProductInDatabase() throws Exception {
        Product updatedProduct = Product.builder()
                .productId(3L)
                .productName("Living Room Chair")
                .description("The best chair in the world.")
                .manufacturer("Chair World")
                .category(HOME_LIVING)
                .price(299.99)
                .build();

        mockMvc.perform(put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedProduct))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("The best chair in the world.")));
    }

    @Test
    public void updateProductForNonExistentId_returnsErrorMessage() throws Exception {
        Product updatedProductWithNonExistentId = Product.builder()
                .productId(17L)
                .productName("Living Room Chair")
                .description("The best chair in the world.")
                .manufacturer("Chair World")
                .category(HOME_LIVING)
                .price(299.99)
                .build();

        mockMvc.perform(put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedProductWithNonExistentId)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Product with id 17 was not found.")));
    }

    @Test
    public void deleteProductById_removesProductById_andReturns204() throws Exception {
        mockMvc.perform(delete("/product/92"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("There is no product with id 92 in the database")));
    }

    @Test
    public void deleteProductById_withNonExistentId_andReturns404() throws Exception {
        mockMvc.perform(delete("/product/3"))
                .andExpect(status().isNoContent());
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