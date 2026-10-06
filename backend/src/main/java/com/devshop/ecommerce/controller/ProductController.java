package com.devshop.ecommerce.controller;

import com.devshop.ecommerce.model.Product;
import com.devshop.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getProducts() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody Product product) {
        return service.create(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        return service.update(id, product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        service.delete(id);
    }
}
