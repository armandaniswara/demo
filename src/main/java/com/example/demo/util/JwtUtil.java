package com.example.demo.util;

import com.example.demo.dto.response.TokenResponse;
import com.example.demo.entity.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 jam
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String getTokenFromBearer(String bearer) {
        String[] token = bearer.split(" ");

        if (token.length != 2 || !token[0].equals("Bearer")) {
            throw new RuntimeException();
        }

        return token[1];
    }

    public TokenResponse generateToken(Users users) {

        Map<String, Object> tokenResponse = new HashMap<>();
        tokenResponse.put("User", users);

        LocalDateTime expire = LocalDateTime.now().plus(EXPIRATION_TIME, ChronoUnit.MINUTES);
        Date expiredDate = Date.from(expire.atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
                .setClaims(tokenResponse)
                .setIssuedAt(new Date())
                .setIssuer("System")
                .setExpiration(expiredDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

        return TokenResponse.builder()
                .token(token)
                .expiresIn(expiredDate)
                .build();
    }


}
