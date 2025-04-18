package com.example.demo.service.impl;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.BaseResponse;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.Util;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public BaseResponse<Object> register(RegisterRequest registerRequest) {

        Users user = new Users();
        user.setName(registerRequest.getName());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(Util.encrypt(registerRequest.getPassword()));

        userRepository.save(user);

        return BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .build();
    }

    @Override
    public BaseResponse<Object> login(LoginRequest loginRequest) {

        Optional<Users>  user = userRepository.findByUsername(loginRequest.getUsername());

        TokenResponse tokenResponse;

        if (user.isPresent() &&  user.get().getPassword().equals(Util.encrypt(loginRequest.getPassword()))) {
            tokenResponse = jwtUtil.generateToken(user.get());
        }  else {
            return BaseResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("User not found")
                    .build();
        }

        return BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(tokenResponse)
                .build();
    }
}
