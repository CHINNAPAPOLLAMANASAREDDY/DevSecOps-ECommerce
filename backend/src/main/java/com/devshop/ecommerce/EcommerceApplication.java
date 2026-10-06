package com.devshop.ecommerce;

import com.devshop.ecommerce.model.Product;
import com.devshop.ecommerce.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedProducts(ProductRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Product("Wireless Headphones", "Electronics", 2499.0,
                        "Comfortable wireless headphones.", "🎧"));
                repository.save(new Product("Smart Watch", "Electronics", 3999.0,
                        "Fitness tracking and smart features.", "⌚"));
                repository.save(new Product("Laptop Backpack", "Accessories", 1499.0,
                        "Water-resistant laptop backpack.", "🎒"));
                repository.save(new Product("Running Shoes", "Fashion", 2299.0,
                        "Lightweight running and walking shoes.", "👟"));
                repository.save(new Product("Coffee Mug", "Home", 499.0,
                        "Simple ceramic desk mug.", "☕"));
            }
        };
    }
}
