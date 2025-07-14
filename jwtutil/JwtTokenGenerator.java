package com.codeera.expensetracker.jwtutil;

import com.codeera.expensetracker.constant.AppConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenGenerator {

    private final String SECRET_KEY = AppConstants.JWT_SECRET_KEY;
    private final long JWT_EXPIRATION = 1000 * 60 *60*16;//* 15;

    private final String APP = "Expense Tracker";
    @Autowired
    private  Environment environment;


    public String generateToken(Authentication authentication) {
        String key = environment.getProperty(AppConstants.JWT_SECRET_KEY, AppConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        System.out.println("Generating token for user: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        return  Jwts.builder()
                .issuer(APP)
                .subject(authentication.getName())
                .claim("username", authentication.getName())
                .claim("authority", authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
               .signWith(secretKey, SignatureAlgorithm.HS256)
               .compact();

    }
}
