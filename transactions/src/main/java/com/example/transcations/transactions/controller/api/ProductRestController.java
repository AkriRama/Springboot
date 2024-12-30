package com.example.transcations.transactions.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transcations.transactions.repository.ProductRepository;
import com.example.transcations.transactions.utils.CustomResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/product")
public class ProductRestController {
    private ProductRepository productRepository;

    @Autowired
    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<Object> get() {
        return CustomResponse.generate(HttpStatus.OK, "Data Found", productRepository.findAll());
    }
}
