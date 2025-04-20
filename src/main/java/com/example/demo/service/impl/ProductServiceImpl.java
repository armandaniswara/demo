package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateProductRequest;
import com.example.demo.dto.response.BaseResponse;
import com.example.demo.dto.response.UserInfo;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final JwtUtil jwtUtil;

    public ProductServiceImpl(ProductRepository productRepository, JwtUtil jwtUtil) {
        this.productRepository = productRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public BaseResponse<Object> createProduct(CreateProductRequest request, String bearer) {
        BaseResponse response;
        try {
            UserInfo userInfo = jwtUtil.getUserInfo(bearer);

            if (userInfo == null) {
                return BaseResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("Failed")
                        .build();
            }

            Product product = new Product();
            product.setCode(request.getCode());
            product.setName(request.getName());
            product.setQuantity(request.getQty());
            productRepository.save(product);

            response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Success create product")
                    .build();
        } catch (Exception ex) {
            response = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(ex.getMessage())
                    .build();
        }

        return response;
    }

    @Override
    public BaseResponse<Object> productList(String bearer) {
        BaseResponse response;
        try {
            UserInfo userInfo = jwtUtil.getUserInfo(bearer);

            if (userInfo == null) {
                return BaseResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("Failed")
                        .build();
            }

            List<Product> productList = productRepository.findAll();

            response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Success product list")
                    .data(productList)
                    .build();

        } catch (Exception ex) {

            response = BaseResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("error get product list : " + ex.getMessage())
                    .build();
        }

        return response;
    }
}
