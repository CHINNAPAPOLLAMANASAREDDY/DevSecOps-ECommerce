package com.devshop.ecommerce.controller;

import com.devshop.ecommerce.model.Product;
import com.devshop.ecommerce.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final ProductRepository productRepository;

    public OrderController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createOrder(@RequestBody OrderRequest order) {
        if (order == null || order.items() == null || order.items().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        double total = 0.0;

        for (OrderItem item : order.items()) {
            if (item.productId() == null || item.quantity() == null || item.quantity() < 1) {
                throw new IllegalArgumentException("Invalid order item");
            }

            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.productId()));

            total += product.getPrice() * item.quantity();
        }

        double roundedTotal = BigDecimal.valueOf(total)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        return Map.of(
                "message", "Order created successfully",
                "total", roundedTotal,
                "items", order.items().size()
        );
    }

    public record OrderRequest(List<OrderItem> items) {}
    public record OrderItem(Long productId, Integer quantity) {}
}
