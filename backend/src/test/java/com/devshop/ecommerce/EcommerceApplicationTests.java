package com.devshop.ecommerce;

import com.devshop.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EcommerceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository repository;

    @Test
    void contextLoads() {
        assertThat(repository.count()).isGreaterThanOrEqualTo(5);
    }

    @Test
    void healthEndpointWorks() {
        ResponseEntity<Map> response =
                restTemplate.getForEntity("/api/health", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("status")).isEqualTo("healthy");
    }

    @Test
    void productsEndpointWorks() {
        ResponseEntity<ProductResponse[]> response =
                restTemplate.getForEntity("/api/products", ProductResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSizeGreaterThanOrEqualTo(5);
    }

    record ProductResponse(Long id, String name, String category, Double price,
                           String description, String image) {}
}
