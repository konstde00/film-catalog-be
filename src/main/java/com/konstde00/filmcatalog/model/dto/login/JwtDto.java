package com.konstde00.filmcatalog.model.dto.login;

import com.konstde00.filmcatalog.model.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@FieldDefaults(level = PRIVATE)
public class JwtDto {

    Long id;

    String name;

    String username;

    @ToString.Exclude
    String accessToken;

    @ToString.Exclude
    String refreshToken;

    String tokenType;

    String profileUrl;

    Collection<Role> roles;

    LocalDateTime accessTokenExpirationTime;

    LocalDateTime accessTokenCreationTime;
}

