package com.api_task_management.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

//    public SecretKey getSignInKey () {
//        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//    }

    public SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken (UserDetails user) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
               .subject(user.getUsername())
//                .claim("payload" , user) // is used to set payload
               .issuedAt(new Date(currentTime))
               .expiration(new Date(currentTime + jwtExpiration))
               .signWith(getSignInKey())
               .compact();
    }

    public Claims extractAllClaims (String token) {
       return Jwts
               .parser()
               .verifyWith(getSignInKey())
               .build()
               .parseSignedClaims(token)
               .getPayload();
    }

    public String extractSubject (String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);

        String subject = claims.getSubject();
        Date expiration = claims.getExpiration();

        return subject != null
                && subject.equals(userDetails.getUsername())
                && expiration != null
                && expiration.after(new Date());
    }

}
