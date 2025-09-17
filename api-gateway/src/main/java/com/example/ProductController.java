package com.example;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/employee")
    public List<Product> getEmployees() {
        return productRepository.findAll();
    }

    @PostMapping("/employee")
    public String addProduct(@RequestBody Product product) {

        return "product added..." + productRepository.save(product);
    }

}
