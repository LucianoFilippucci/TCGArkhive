package it.lucianofilippucci.tcgarkhive.configuration.security.authentication;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import it.lucianofilippucci.tcgarkhive.entity.RolesEntity;
import it.lucianofilippucci.tcgarkhive.entity.UserEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {
    @Value("${tcgarkhive.auth.access-token-secret}")
    private String accessToken;
    @Value("${tcgarkhive.auth.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${tcgarkhive.auth.refresh-token-secret}")
    private String refreshToken;
    @Value("${tcgarkhive.auth.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @Value("${tcgarkhive.auth.pwd-reset-secret}")
    private String pwdResetToken;
    @Value("${tcgarkhive.auth.pwd-reset-token-expiration}")
    private Long pwdResetTokenExpiration;
    @Value("${tcgarkhive.auth.pwd-reset-endpoint}")
    private String pwdResetEndpoint;

    private SecretKey accessKey;
    private SecretKey refreshKey;
    private SecretKey pwdResetKey;

    @PostConstruct
    public void init() {
        this.accessKey = Keys.hmacShaKeyFor(accessToken.getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(refreshToken.getBytes(StandardCharsets.UTF_8));
        this.pwdResetKey = Keys.hmacShaKeyFor(pwdResetToken.getBytes(StandardCharsets.UTF_8));
    }

    private List<String> getRolesFromEntity(UserEntity userEntity) {
        return userEntity.getRoles()
                .stream()
                .map(RolesEntity::getName)
                .toList();
    }

    public String generateAccessToken(UserEntity user, Long sessionId) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .claim("email", user.getEmail())
                .claim("roles", this.getRolesFromEntity(user))
                .claim("sessionId", sessionId)
                .setExpiration(new Date((new Date()).getTime() + this.accessTokenExpiration))
                .signWith(this.accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(this.accessKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            System.out.println("Invalid JWT Signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.accessKey).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public String generateRefreshToken(UserEntity user, Long sessionId) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + this.refreshTokenExpiration))
                .claim("email", user.getEmail())
                .claim("roles", this.getRolesFromEntity(user))
                .claim("sessionId", sessionId)
                .signWith(this.refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            System.out.println("Invalid Refresh token Signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid Refresh token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Expired Refresh token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported Refresh token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    public String getRefreshTokenSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long getSessionIdFromRefreshToken(String token) {
        return Jwts.parserBuilder().build()
                .parseClaimsJws(token)
                .getBody()
                .get("sessionId", Long.class);
    }
}
