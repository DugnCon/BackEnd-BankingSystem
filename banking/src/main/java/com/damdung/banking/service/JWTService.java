package com.damdung.banking.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JWTService {
    private final String SECRET_KEY;
    private final SecretKey key;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    private static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    public JWTService(@Value("${jwt.secret}") String secretKey) {
        this.SECRET_KEY = secretKey;
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Tạo token với claims
     * */
    public String getTokenWithClaims(Map<String,Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject((String) claims.get("email"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    /**
     * Refresh token
     * */
    public String refreshToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(SECRET_KEY)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    /**
     * Giải mã Claims để lấy data trong các key
     * */
    public Claims parseTokenWithClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check thời gian tồn tại của token
     * */
    public boolean isTokenExpired(String token) {
        try {
            Date exp = parseTokenWithClaims(token).getExpiration();
            return exp.before(new Date());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Check xem token có lỗi hay không
     * */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Map<String,Object> claims = parseTokenWithClaims(token);
            String emailToken = (String) claims.get("email");
            return (emailToken.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
}
