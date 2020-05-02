package com.diamante.orderingsystem.controller.product;

import com.diamante.orderingsystem.config.JwtTokenProvider;
import com.diamante.orderingsystem.entity.Category;
import com.diamante.orderingsystem.entity.Product;
import com.diamante.orderingsystem.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static com.diamante.orderingsystem.entity.Category.ELECTRONICS;
import static com.diamante.orderingsystem.entity.Category.HOME_LIVING;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductController.class)
@ContextConfiguration
public class ProductControllerTest {

    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    private JwtTokenProvider mockJwtTokenProvider;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    Product product1;

    Product product2;

    Product product3;

    Product product4;

    @Before
    public void setUp() throws Exception {
        createProductsForTesting();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void getAllProducts_returnsAllProducts_statusIsOK() throws Exception {
        when(productService.findAllProducts())
                .thenReturn(Arrays.asList(product1, product2, product3, product4));

        mockMvc.perform(get("/product/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].productName", is(product1.getProductName())))
                .andExpect(jsonPath("$[2].description", is(product3.getDescription())));
    }

    @Test
    public void getAllProducts_returnsProductsFilteredByCategory_whenRequestParamCatIsIncluded_statusIsOK() throws Exception {
        when(productService.findAllProductsByCategory(ELECTRONICS))
                .thenReturn(Arrays.asList(product1, product4));

        mockMvc.perform(get("/product/list?cat=ELECTRONICS")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName", is(product1.getProductName())))
                .andExpect(jsonPath("$[1].productName", is(product4.getProductName())));
    }

    @Test
    public void getAllProducts_returnsACustomErrorMessage_whenParameterCategoryIsInvalid_statusIsBadRequest() throws Exception {
        when(productService.findAllProductsByCategory(ELECTRONICS))
                .thenReturn(Arrays.asList(product1, product4));
        mockMvc.perform(get("/product/list?cat=nonsense")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        is("Valid Categories for our products: " +
                                "ELECTRONICS, BOOKS, HOME_LIVING, MOVIES_MUSIC_GAMES, & CLOTHING_SHOES_JEWELERY_WATCHES")));
    }

    @Test
    public void getAllProducts_returnsProductsFilteredByPrice_whenParameterPriceIsProvided_statusIsOk() throws Exception {
        when(productService.findAllProductsByPriceLessThan(46))
                .thenReturn(Arrays.asList(product2, product3));
        mockMvc.perform(get("/product/list?price=46")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName", is(product2.getProductName())))
                .andExpect(jsonPath("$[1].productName", is(product3.getProductName())));
    }

    @Test
    public void getAllProducts_returnsACustomErrorMessage_whenParameterPriceIsInvalid_statusIsBadRequest() throws Exception {
        when(productService.findAllProductsByCategory(ELECTRONICS))
                .thenReturn(Arrays.asList(product1, product4));
        mockMvc.perform(get("/product/list?price=number")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        is("Optional Parameter, price, must be a double. " +
                                "Example: /product/list?price=71.99")));
    }

    @Test
    public void getAllProducts_whenParameterKeyIsInvalid_returnsAllProducts() throws Exception {
        when(productService.findAllProducts())
                .thenReturn(Arrays.asList(product1, product2, product3, product4));

        mockMvc.perform(get("/product/list?param=value")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[1].productName", is(product2.getProductName())))
                .andExpect(jsonPath("$[3].description", is(product4.getDescription())));
    }

    @Test
    public void getProductById_returnsProduct_statusIsOK() throws Exception {
        when(productService.findByProductId(4L))
                .thenReturn(product4);

        mockMvc.perform(get("/product?id=4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is(product4.getProductName())))
                .andExpect(jsonPath("$.description", is(product4.getDescription())))
                .andExpect(jsonPath("$.category", is(product4.getCategory().name())))
                .andExpect(jsonPath("$.manufacturer", is(product4.getManufacturer())));
    }

    @Test
    public void getProductByProductName_returnsProduct_statusIsOk() throws Exception {
        when(productService.findProductByName("Ipod"))
                .thenReturn(product1);

        mockMvc.perform(get("/product?name=Ipod")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is(product1.getProductName())))
                .andExpect(jsonPath("$.description", is(product1.getDescription())));
    }

    @Test
    public void createProduct_savesTheProductInTheDatabase_statusIsCreated() throws Exception {
        Product product5 = Product.builder()
                .productName("Chair")
                .description("Modern kitchen table chair.")
                .manufacturer("Ashley Furniture")
                .category(HOME_LIVING)
                .price(249.99)
                .build();

        when(productService.saveProduct(product5))
                .thenReturn(product5);

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product5)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName", is(product5.getProductName())))
                .andExpect(jsonPath("$.description", is(product5.getDescription())));
    }

    @Test
    public void updateProduct_updatesTheProductInTheDatabase_statusIsOk() throws Exception {
        Product updatedProduct = Product.builder()
                .productId(1L)
                .productName("Ipod")
                .description("Music player for all ages!")
                .manufacturer("Apple")
                .category(ELECTRONICS)
                .price(155.99)
                .build();

        when(productService.updateProduct(updatedProduct))
                .thenReturn(updatedProduct);

        mockMvc.perform(put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedProduct)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(updatedProduct.getPrice())))
                .andExpect(jsonPath("$.description", is(updatedProduct.getDescription())));
    }

    @Test
    public void updateProduct_forNonexistentId_returnsErrorMessage() throws Exception {
        Product productWithWrongId = Product.builder()
                .productId(8L)
                .productName("Ipod")
                .description("Music player for all ages!")
                .manufacturer("Apple")
                .category(ELECTRONICS)
                .price(155.99)
                .build();

        when(productService.updateProduct(any(Product.class))).thenReturn(null);

        mockMvc.perform(put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productWithWrongId)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Product with id 8 was not found.")));
    }

    @Test
    public void updateCustomerReturnsEntityValidationException_whenProductDescriptionIsMoreThan40Characters() throws Exception {
        Product productWithLongDescription = Product.builder()
                .productId(2L)
                .productName("Merlin")
                .description("Novel about the adventures of King Arthur and a wizard named Merlin. " +
                        "A great read for all ages!!!")
                .manufacturer("Penguin Books")
                .category(Category.BOOKS)
                .price(7.99)
                .build();

        when(productService.updateProduct(any(Product.class))).thenThrow(TransactionSystemException.class);

        mockMvc.perform(put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productWithLongDescription)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        is("The field description with value Novel about the adventures of King Arthur and a " +
                                "wizard named Merlin. A great read for all ages!!! does not meet requirements. The description must be no longer than 40 characters")));
    }

    @Test
    public void updateCustomerReturnsTransactionSystemException_dueToDatabaseTransactionIssue() throws Exception {
        when(productService.updateProduct(any(Product.class))).thenThrow(TransactionSystemException.class);

        mockMvc.perform(put("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product1)))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message",
                        is("Product was not updated. Error while committing the transaction.")));
    }

    @Test
    public void createProductReturnsErrorMessage_whenProductIdIsIncluded() throws Exception {
        Product productWithExplicitId = Product.builder()
                .productId(5L)
                .description("Music player")
                .manufacturer("Apple")
                .category(ELECTRONICS)
                .price(120.00)
                .build();

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productWithExplicitId)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", is("Product id is auto generated. Please remove this field from your request.")));
    }

    @Test
    public void createProductReturnsValidationErrorMessage_whenProductNameIsNull() throws Exception {
        Product invalidProduct = Product.builder()
                .description("Music player")
                .manufacturer("Apple")
                .category(ELECTRONICS)
                .price(120.00)
                .build();

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidProduct)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field productName with value null does not meet requirements. " +
                        "Product name is a required field")));
    }

    @Test
    public void createProductReturnsValidationErrorMessage_whenDescriptionIsNull() throws Exception {
        Product invalidProduct = Product.builder()
                .productName("Ipod")
                .manufacturer("Apple")
                .category(ELECTRONICS)
                .price(120.00)
                .build();

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidProduct)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field description with value null does not meet requirements. " +
                        "Description is a required field")));
    }

    @Test
    public void createProductReturnsValidationErrorMessage_whenManufacturerIsNull() throws Exception {
        Product invalidProduct = Product.builder()
                .productName("Ipod")
                .description("Music player")
                .category(ELECTRONICS)
                .price(120.00)
                .build();

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidProduct)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field manufacturer with value null does not meet requirements. " +
                        "Manufacturer is a required field")));
    }

    @Test
    public void createProductReturnsValidationErrorMessage_whenCategoryIsNull() throws Exception {
        Product invalidProduct = Product.builder()
                .productName("Ipod")
                .description("Music player")
                .manufacturer("Apple")
                .price(120.00)
                .build();

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidProduct)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field category with value null does not meet requirements. " +
                        "Category is a required field")));
    }

    @Test
    public void createProductReturnsValidationErrorMessage_whenPriceIsNull() throws Exception {
        Product invalidProduct = Product.builder()
                .productName("Ipod")
                .description("Music player")
                .manufacturer("Apple")
                .category(ELECTRONICS)
                .build();

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidProduct)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The field price with value null does not meet requirements. " +
                        "Price is a required field")));
    }

    @Test
    public void createCustomerReturnsGenericErrorMessage_whenAnyBroadExceptionIsThrown() throws Exception {
        Product invalidProduct = Product.builder()
                .productName("Ipod")
                .description("Music player")
                .manufacturer("Apple")
                .price(30.90)
                .category(ELECTRONICS)
                .build();

        when(productService.saveProduct(invalidProduct)).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidProduct)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message",
                        is("Your request could not be made. Please try again.")));
    }

    @Test
    public void deleteProduct() throws Exception {
        doNothing().when(productService).deleteProductById(anyLong());

        mockMvc.perform(delete("/product/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteProduct_returnsDataRetrievalFailureErrorMessage_whenThereIsNoProductToDelete() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(productService).deleteProductById(23L);

        mockMvc.perform(delete("/product/23"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("There is no product with id 23 in the database")));
    }

    private void createProductsForTesting() {
        product1 = Product.builder()
                .productId(1L)
                .productName("Ipod")
                .description("Music player")
                .manufacturer("Apple")
                .category(ELECTRONICS)
                .price(120.00)
                .build();

        product2 = Product.builder()
                .productId(2L)
                .productName("Merlin")
                .description("Novel about the adventures of King Arthur and a wizard named Merlin.")
                .manufacturer("Penguin Books")
                .category(Category.BOOKS)
                .price(6.99)
                .build();

        product3 = Product.builder()
                .productId(3L)
                .productName("Men's Black Watch")
                .description("A black watch")
                .manufacturer("IZOD")
                .category(Category.CLOTHING_SHOES_JEWELERY_WATCHES)
                .price(45.79)
                .build();

        product4 = Product.builder()
                .productId(4L)
                .productName("Flat Screen TV")
                .description("OLED High Definition TV")
                .manufacturer("Samsung")
                .category(ELECTRONICS)
                .price(599.99)
                .build();
    }
}