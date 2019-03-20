package com.diamante.orderingsystem.service.product;

import com.diamante.orderingsystem.entity.Category;
import com.diamante.orderingsystem.entity.Product;
import com.diamante.orderingsystem.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAllProducts() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public List<Product> findAllProductsByCategory(Category category) {
        return productRepository.findProductsByCategory(category);
    }

    @Override
    public List<Product> findAllProductsByPriceLessThan(double price) {
        return productRepository.findProductsByPriceLessThan(price);
    }

    @Override
    public Product findByProductId(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.orElse(null);
    }

    @Override
    public Product findProductByName(String productName) {
        Optional<Product> optionalProduct = productRepository.findProductByProductName(productName);
        return optionalProduct.orElse(null);
    }

    @Override
    public Product updateProduct(Product updatedProduct) {
        Optional<Product> originalProduct = productRepository.findById(updatedProduct.getProductId());

        if (!originalProduct.isPresent()) {
            return null;
        }

        Product product = originalProduct.get();

        product.setProductName(updatedProduct.getProductName());
        product.setProductImage(updatedProduct.getProductImage());
        product.setDescription(updatedProduct.getDescription());
        product.setManufacturer(updatedProduct.getManufacturer());
        product.setCategory(updatedProduct.getCategory());
        product.setPrice(updatedProduct.getPrice());
        product.setQuantity(updatedProduct.getQuantity());

        return productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    @Override
    public void resetAllProductIds() {
        productRepository.resetAllProductIds();
    }
}
