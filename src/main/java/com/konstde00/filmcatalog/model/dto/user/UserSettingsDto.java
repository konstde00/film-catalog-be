package com.konstde00.filmcatalog.model.dto.user;

import com.konstde00.filmcatalog.model.enums.CountryCode;
import lombok.Builder;
import lombok.Value;

import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Value
@Builder
public class UserSettingsDto {

    String username;

    String oldPassword;

    String newPassword;

    String avatarUrl;

    @Enumerated(STRING)
    CountryCode country;

    LanguageDto language;
}
