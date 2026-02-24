package com.studyflow.sf_backend.service;

import com.studyflow.sf_backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long EXPIRATION_TIME;

    /**
     * Generates a JWT token for the given user.
     *
     * @param user the user to generate token for (email is used as token subject)
     * @return JWT token string
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Extracts the email subject from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the email subject embedded in the token
     */
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Validates a JWT token for a given user.
     *
     * @param token the JWT token to validate
     * @param user the user to validate the token against
     * @return true if token is valid (not expired and email matches), false otherwise
     */
    public boolean isTokenValid(String token, User user) {
        final String email = extractEmail(token);
        return (email.equals(user.getEmail()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
