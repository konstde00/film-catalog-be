package com.konstde00.filmcatalog.config;

import com.konstde00.filmcatalog.service.UserService;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collection;

import static com.konstde00.filmcatalog.model.enums.TokenType.ACCESS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final String jwtSecret;
    private final UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   String jwtSecret,
                                   UserService userService) {
        super(authenticationManager);
        this.jwtSecret = jwtSecret;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        var authentication = parseToken(request);

        if (authentication != null)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        else
            SecurityContextHolder.clearContext();
        filterChain.doFilter(request, response);
    }

    @SneakyThrows
    private UsernamePasswordAuthenticationToken parseToken(HttpServletRequest request) {

        var token = request.getHeader(AUTHORIZATION);

        log.info("Authentication token: " + token);

        if (token != null && token.startsWith("Bearer ")) {

            try {
                var claims = token.replace("Bearer ", "");

                var claimsJws = Jwts.parserBuilder()
                        .requireIssuer(ACCESS.name())
                        .setSigningKey(jwtSecret.getBytes())
                        .build()
                        .parseClaimsJws(claims);

                var userId = Long.parseLong(claimsJws.getBody().getSubject());

                var user = userService.getById(userId);

                if (user.getIsBlocked()) {
                    throw new AccessDeniedException("User is blocked");
                }

                var authorities = ((Collection<String>) claimsJws.getBody().get("role")).stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                return new UsernamePasswordAuthenticationToken(userId, null, authorities);

            } catch (Exception exception) {

                log.error("Error while parsing token", exception);
            }
        }

        return null;
    }
}
