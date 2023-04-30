package com.konstde00.filmcatalog.mapper;

import com.konstde00.filmcatalog.model.dto.user.LanguageDto;
import com.konstde00.filmcatalog.model.dto.user.UserGeneraInfoDto;
import com.konstde00.filmcatalog.model.dto.user.UserSettingsDto;
import com.konstde00.filmcatalog.model.entity.User;
import com.konstde00.filmcatalog.model.enums.Language;
import com.konstde00.filmcatalog.model.enums.UserRegistrationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.konstde00.filmcatalog.model.enums.Role.ROLE_USER;
import static java.time.LocalDateTime.now;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toEntity(String email, String name, String username, String password, UserRegistrationType registrationType) {
        return User
                .builder()
                .email(email)
                .name(name)
                .username(username)
                .password(password)
                .registrationType(registrationType)
                .roles(singletonList(ROLE_USER))
                .createdAt(now())
                .build();
    }


    public UserGeneraInfoDto toGeneralInfoDto(User user) {
        return UserGeneraInfoDto
                .builder()
                .id(user.getId())
                .languages(Arrays.stream(Language.values()).map(this::toLanguage).collect(toList()))
                .registrationType(user.getRegistrationType())
                .build();
    }

    public UserSettingsDto toSettingsDto(User user) {
        return UserSettingsDto
                .builder()
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .oldPassword("")
                .newPassword("")
                .country(user.getNativeCountryCode())
                .build();
    }

    public LanguageDto toLanguage(Language language) {
        return LanguageDto
                .builder()
                .name(language.getName())
                .nativeName(language.getNativeName())
                .emoji(language.getEmoji())
                .tag(language)
                .build();
    }
}
