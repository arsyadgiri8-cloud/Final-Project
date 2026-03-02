package com.arsyad.asset_management.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET_KEY =
            "my-super-secret-key-my-super-secret-key";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }


    // GENERATE TOKEN

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignKey())
                .compact();
    }

    // EXTRACT USERNAME

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }


    // VALIDATE TOKEN

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }


    // CHECK EXPIRATION

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }


    // EXTRACT CLAIMS

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
