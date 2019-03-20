package com.diamante.orderingsystem.repository.product;

import com.diamante.orderingsystem.entity.Category;
import com.diamante.orderingsystem.entity.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findProductsByCategory(Category category);
    List<Product> findProductsByPriceLessThan(double price);
    Optional<Product> findProductByProductName(String productName);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE products_product_id_seq RESTART WITH 1", nativeQuery = true)
    void resetAllProductIds();
}
