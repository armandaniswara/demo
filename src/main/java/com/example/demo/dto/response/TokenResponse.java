package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TokenResponse {

    private String token;

    private Date expiresIn;

}
