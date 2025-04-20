package com.example.demo.service;

import com.example.demo.dto.request.CreateProductRequest;
import com.example.demo.dto.response.BaseResponse;

public interface ProductService {

    BaseResponse<Object> createProduct(CreateProductRequest request, String bearer);

    BaseResponse<Object> productList(String bearer);

}

