package com.devshop.ecommerce.service;

import com.devshop.ecommerce.model.Product;
import com.devshop.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    public Product create(Product product) {
        product.setId(null);
        return repository.save(product);
    }

    public Product update(Long id, Product product) {
        Product existing = getById(id);
        existing.setName(product.getName());
        existing.setCategory(product.getCategory());
        existing.setPrice(product.getPrice());
        existing.setDescription(product.getDescription());
        existing.setImage(product.getImage());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        repository.deleteById(id);
    }
}
