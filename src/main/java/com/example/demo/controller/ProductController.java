package com.example.demo.controller;

import com.example.demo.dto.request.CreateProductRequest;
import com.example.demo.service.ProductService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductRequest request,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String bearer) {
        var response = productService.createProduct(request, bearer);

        return ResponseEntity.status(response.getStatus())
                .body(response);

    }
}
