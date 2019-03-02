//package com.diamante.orderingsystem.controller.product;
//
//import com.diamante.orderingsystem.entity.product;
//import com.diamante.orderingsystem.service.product.ProductService;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping("/product")
////TODO
////@Api
//public class ProductController {
//
//    private final ProductService productService;
//
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }
//
//    @GetMapping
//    public List<product> getAllProducts() {
//        return productService.findAllProducts();
//    }
//
//    @GetMapping("/{id}")
//    public product getProductById(@PathVariable("id") Long id){
//        product product = productService.findByProductId(id);
//        return product;
//    }
//
////    @GetMapping("/{category}")
////    public Optional<product> getProductByCategory(@PathVariable("category") String category) {
////        return productService.findByCategory(category);
////    }
//
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public product createProduct(@Valid @RequestBody product product) {
//        return productService.saveProduct(product);
//    }
//
//
//
//
//
//}
