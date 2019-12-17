package com.diamante.orderingsystem.bootstrap;

import com.diamante.orderingsystem.entity.Category;
import com.diamante.orderingsystem.entity.Product;
import com.diamante.orderingsystem.service.product.ProductService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.lang.Double.valueOf;

@Component
@Profile("test")
@Slf4j
@Transactional
public class FakeProductLoader implements CommandLineRunner {

    private static final String DOLLAR_AMOUNT = "##.##";
    private List<String> usedNames = new ArrayList<>();

    private final ProductService productService;
    private final Faker faker;

    public FakeProductLoader(ProductService productService, Faker faker) {
        this.productService = productService;
        this.faker = faker;
    }

    @Override
    public void run(String... args) throws Exception {

        IntStream.range(0, 300).forEach(it -> {
            Product randomBook = Product.builder()
                    .productName(faker.book().title())
                    .description(faker.funnyName().name())
                    .manufacturer(faker.book().publisher())
                    .category(Category.BOOKS)
                    .price(valueOf(faker.numerify(DOLLAR_AMOUNT)))
                    .quantity(faker.number().numberBetween(50, 250))
                    .build();

            Product randomInstrument = Product.builder()
                    .productName(faker.music().instrument())
                    .description(faker.funnyName().name())
                    .manufacturer(faker.company().name())
                    .category(Category.MOVIES_MUSIC_GAMES)
                    .price(valueOf(faker.numerify(DOLLAR_AMOUNT)))
                    .quantity(faker.number().numberBetween(50, 250))
                    .build();

            Product randomProduct = Product.builder()
                    .productName(faker.commerce().productName())
                    .description(faker.funnyName().name())
                    .manufacturer(faker.company().name())
                    .category(Category.HOME_LIVING)
                    .price(valueOf(faker.numerify(DOLLAR_AMOUNT)))
                    .quantity(faker.number().numberBetween(50, 250))
                    .build();

            Product randomProduct2 = Product.builder()
                    .productName(faker.commerce().productName())
                    .description(faker.funnyName().name())
                    .manufacturer(faker.company().name())
                    .category(Category.ELECTRONICS)
                    .price(valueOf(faker.numerify(DOLLAR_AMOUNT)))
                    .quantity(faker.number().numberBetween(50, 250))
                    .build();

            Optional<Product> savedProduct1 = getSavedProduct(randomBook);
            Optional<Product> savedProduct2 = getSavedProduct(randomInstrument);
            Optional<Product> savedProduct3 = getSavedProduct(randomProduct);
            Optional<Product> savedProduct4 = getSavedProduct(randomProduct2);

            savedProduct1.ifPresent(product -> usedNames.add(product.getProductName()));
            savedProduct2.ifPresent(product -> usedNames.add(product.getProductName()));
            savedProduct3.ifPresent(product -> usedNames.add(product.getProductName()));
            savedProduct4.ifPresent(product -> usedNames.add(product.getProductName()));
        });

        log.info("Data load complete! {} items saved!", usedNames.size());
    }

    private Optional<Product> getSavedProduct(Product product) {
        return isNameAvailable(product.getProductName()) ? Optional.of(productService.saveProduct(product)) : Optional.empty();
    }

    private Boolean isNameAvailable(String productName) {
        return !usedNames.contains(productName);
    }
}
