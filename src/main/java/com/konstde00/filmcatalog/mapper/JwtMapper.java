package com.konstde00.filmcatalog.mapper;

import com.konstde00.filmcatalog.model.dto.login.JwtDto;
import com.konstde00.filmcatalog.model.entity.RefreshToken;
import com.konstde00.filmcatalog.model.entity.User;
import com.konstde00.filmcatalog.model.enums.TokenType;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.konstde00.filmcatalog.model.enums.TokenType.ACCESS;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;

public class JwtMapper {

    public static String token(User user, Long expirationTime, SecretKey secretKey) {
        return Jwts.builder()
                .signWith(secretKey, HS512)
                .setIssuer(ACCESS.name())
                .setSubject(Long.toString(user.getId()))
                .setExpiration(new Date(expirationTime))
                .claim("role", user.getRoles())
                .compact();
    }

    public static String refreshToken(User user, Long expirationTime, SecretKey secretKey, RefreshToken refresh) {
        return Jwts.builder()
                .signWith(secretKey, HS512)
                .setId(Long.toString(refresh.getId()))
                .setSubject(Long.toString(user.getId()))
                .setIssuer(TokenType.REFRESH.name())
                .setExpiration(new Date(expirationTime))
                .compact();
    }

    public static RefreshToken refreshTokenEntity(User user, Long expirationTime) {
        return RefreshToken
                .builder()
                .token("token")
                .createdAt(now())
                .expiredAt(ofEpochMilli(expirationTime).atZone(systemDefault()).toLocalDateTime())
                .user(user)
                .build();
    }

    public static JwtDto toJwtDto(User user, Long tokenExpirationTime, String token, String refreshToken) {
        return JwtDto.builder()
                .id(user.getId())
                .accessToken(token)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .roles(user.getRoles())
                .accessTokenCreationTime(now())
                .accessTokenExpirationTime(ofEpochMilli(tokenExpirationTime).atZone(systemDefault()).toLocalDateTime())
                .name(user.getName())
                .username(user.getUsername())
                .profileUrl("https://filmcatalog.s3.amazonaws.com/users/" + user.getId() + "/avatar")
                .build();
    }
}
