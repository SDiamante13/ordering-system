package com.diamante.orderingsystem.controller.product;

import com.diamante.orderingsystem.controller.AutoGeneratedIdException;
import com.diamante.orderingsystem.entity.Category;
import com.diamante.orderingsystem.entity.Product;
import com.diamante.orderingsystem.service.product.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.diamante.orderingsystem.utils.ExceptionUtils.createErrorMessageAndThrowEntityValidationException;

@RestController
@RequestMapping("/product")
@Api(value = "Product API", description = "Store new products and retrieve product info from the available categories.")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "View a list of all products", notes = "Optional Parameters: ?cat=Category or ?price=43.99")
    @GetMapping("/list")
    public List<Product> getAllProductsByParam(@RequestParam(required = false) Category cat, @RequestParam(required = false) Double price) {
        if (cat != null) {
            return productService.findAllProductsByCategory(cat);
        } else if (price != null) {
            return productService.findAllProductsByPriceLessThan(price);
        } else {
            return productService.findAllProducts();
        }
    }

    @ApiOperation(value = "View product by searching using the product id or name", notes = "Must use request parameters: `id` or `name`.")
    @GetMapping()
    public Product getProductByIdOrName(@RequestParam(required = false) Long id, @RequestParam(required = false) String name) throws Exception {
        if (id != null) {
            return productService.findByProductId(id);
        } else if (name != null) {
            return productService.findProductByName(name);
        } else {
            throw new NoSuchMethodException("Unable to process request.");
        }
    }

    @ApiOperation(value = "View product by searching using the product Id")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping()
    public Product createProduct(@Valid @RequestBody Product product, BindingResult result) {
        if (product.getProductId() != null) {
            throw new AutoGeneratedIdException("Product id is auto generated. Please remove this field from your request.");
        }

        if (result.hasErrors()) {
            createErrorMessageAndThrowEntityValidationException(result);
        }

        return productService.saveProduct(product);
    }


    @ApiOperation(value = "Updates the product given the correct id.")
    @PutMapping()
    public Product updateProduct(@Valid @RequestBody Product product, BindingResult result) {
        if (result.hasErrors()) {
            createErrorMessageAndThrowEntityValidationException(result);
        }

        Product updatedProduct;

        try {
            updatedProduct = productService.updateProduct(product);
        } catch (TransactionSystemException ex) {
            throw new TransactionSystemException("Product was not updated. Error while committing the transaction.");
        }

        if (updatedProduct == null) {
            throw new ProductNotFoundException("Product with id " + product.getProductId() + " was not found.");
        }

        return updatedProduct;
    }

    @ApiOperation(value = "Deletes the product given an id")
    @ApiResponse(code = 204, message = "Product deleted, no content.")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        try {
            productService.deleteProductById(id);
        } catch (DataRetrievalFailureException ex) {
            throw new DataRetrievalFailureException("There is no product with id " + id + " in the database");
        }
    }
}