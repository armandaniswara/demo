package com.example.demo.service;


import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.BaseResponse;

public interface UserService {

    BaseResponse<Object> register(RegisterRequest registerRequest);

    BaseResponse<Object> login(LoginRequest loginRequest);

}
