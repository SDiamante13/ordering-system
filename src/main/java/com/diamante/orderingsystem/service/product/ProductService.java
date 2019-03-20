package com.diamante.orderingsystem.service.product;

import com.diamante.orderingsystem.entity.Category;
import com.diamante.orderingsystem.entity.Product;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);
    List<Product> findAllProducts();
    List<Product> findAllProductsByCategory(Category category);
    List<Product> findAllProductsByPriceLessThan(double price);
    Product findProductByName(String productName);
    Product findByProductId(Long productId);
    Product updateProduct(Product updateProduct);
    void deleteProductById(Long id);
    void deleteAllProducts();
    void resetAllProductIds();
}
