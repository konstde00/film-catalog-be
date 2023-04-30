package com.konstde00.filmcatalog.model.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {

    @NotNull
    private String refreshToken;
}
