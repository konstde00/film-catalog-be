package com.konstde00.filmcatalog.model.dto.user;

import com.konstde00.filmcatalog.model.enums.CountryCode;
import lombok.Builder;
import lombok.Value;

import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Value
@Builder
public class UserProfileDto {

    Long userId;

    String username;

    String avatarUrl;

    boolean online;

    @Enumerated(STRING)
    CountryCode country;

    String lastOnline;
}
