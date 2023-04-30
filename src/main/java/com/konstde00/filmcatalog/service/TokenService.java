package com.konstde00.filmcatalog.service;

import com.konstde00.filmcatalog.mapper.JwtMapper;
import com.konstde00.filmcatalog.model.dto.login.JwtDto;
import com.konstde00.filmcatalog.model.entity.RefreshToken;
import com.konstde00.filmcatalog.model.entity.User;
import com.konstde00.filmcatalog.model.exception.JwtException;
import com.konstde00.filmcatalog.model.exception.NotValidException;
import com.konstde00.filmcatalog.model.exception.ResourceNotFoundException;
import com.konstde00.filmcatalog.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.lang.System.currentTimeMillis;
import static javax.persistence.CacheStoreMode.REFRESH;
import static org.springframework.security.crypto.bcrypt.BCrypt.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PropertySource(value = "classpath:application.yml")
public class TokenService {

    @NonFinal
    @Value("${jwt.secret}")
    String jwtSecret;

    @NonFinal
    @Value("${token.expiration.time.ms}")
    Long tokenExpirationTimeMs;

    @NonFinal
    @Value("${token.refresh.expiration.time.ms}")
    Long refreshTokenExpirationTimeMs;

    RefreshTokenRepository refreshTokenRepository;

    public TokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public JwtDto getAccessAndRefreshTokens(User user, String refreshToken) {

        log.info("'getAccessAndRefreshTokens' invoked for user - {}", user);

        long tokenExpirationTime = currentTimeMillis() + tokenExpirationTimeMs;
        var token = generateToken(user, tokenExpirationTime);
        var jwtDto = JwtMapper.toJwtDto(user, tokenExpirationTime, token, refreshToken);

        log.info("'getAccessAndRefreshTokens' returned 'Success'");

        return jwtDto;
    }

    public String generateRefreshToken(User user) {

        log.info("'generateRefreshToken' invoked with user - {}", user);

        var secretKey = hmacShaKeyFor(jwtSecret.getBytes());
        var expirationTime = currentTimeMillis() + refreshTokenExpirationTimeMs;

        var savedToken = refreshTokenRepository.save(JwtMapper.refreshTokenEntity(user, expirationTime));

        var token = JwtMapper.refreshToken(user, expirationTime, secretKey, savedToken);

        var hashedToken = bcryptToken(token);

        refreshTokenRepository.update(hashedToken, savedToken.getId());

        log.info("'generateRefreshToken' returned 'Success'");

        return token;
    }

    @SneakyThrows(MalformedJwtException.class)
    public RefreshToken refreshToken(String token) {

        log.info("'refreshToken' invoked");

        var refreshToken = new RefreshToken();

        try {

            var claimsJws = parseRefreshToken(token);

            var userId = Long.parseLong(claimsJws.getBody().getSubject());
            var tokenId = Long.parseLong(claimsJws.getBody().getId());

            refreshToken = refreshTokenRepository.findByUserIdAndId(userId, tokenId)
                    .orElseThrow(() -> new ResourceNotFoundException("User doesn`t have such refresh token"));

            validateToken(token, refreshToken.getToken());

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new JwtException("Token is not valid");
        }

        log.info("'refreshToken' returned 'Success'");

        return refreshToken;
    }

    public void revoke(Long userId, String refreshToken) {

        log.info("'revoke' invoked for user with id - {}", userId);

        try {

            var claimsJws = parseRefreshToken(refreshToken);
            var tokenId = Long.parseLong(claimsJws.getBody().getId());

            refreshTokenRepository.deleteByUserIdAndId(userId, tokenId);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new JwtException("Token is not valid");
        }

        log.info("'revoke' returned 'Success'");
    }

    private Jws<Claims> parseRefreshToken(String refreshToken) {
        return Jwts.parserBuilder()
                .requireIssuer(REFRESH.name())
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(refreshToken);
    }

    private void validateToken(String inputToken, String correctToken) {

        var tokenValidation = checkpw(inputToken, correctToken);

        if (!tokenValidation) {
            log.error("Refresh token is incorrect.");
            throw new NotValidException("Refresh token is incorrect.");
        }
    }

    private String generateToken(User user, long tokenExpirationTime) {

        log.info("'generateToken' invoked for user - {}", user);

        var secretKey = hmacShaKeyFor(jwtSecret.getBytes());
        var token = JwtMapper.token(user, tokenExpirationTime, secretKey);

        log.info("'generateToken' returned 'Success'");

        return token;
    }

    private String bcryptToken(String token) {
        return hashpw(token, gensalt());
    }
}
