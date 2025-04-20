package com.example.demo.util;

import com.example.demo.dto.response.TokenResponse;
import com.example.demo.dto.response.UserInfo;
import com.example.demo.entity.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    public String getUserinfoFromToken(String bearer) {
        String token = getTokenFromBearer(bearer);

        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public UserInfo getUserInfo(String token) {
        UserInfo userInfo = null;
        try {
            userInfo = UserInfo.builder()
                    .name(getUserinfoFromToken(token))
                    .build();
        } catch (ExpiredJwtException e) {
            log.error("Token expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT: " + e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT: " + e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Token is null or empty: " + e.getMessage());
        }
        return userInfo; // Token tidak valid
    }

}
