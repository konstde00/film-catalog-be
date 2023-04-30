package com.konstde00.filmcatalog.model.dto.user;

import com.konstde00.filmcatalog.model.enums.UserRegistrationType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalTime;
import java.util.Collection;

@Value
@Builder
public class UserGeneraInfoDto {

    Long id;

    String deviceToken;

    Collection<LanguageDto> languages;

    UserRegistrationType registrationType;

    LocalTime reminderTime;
}
