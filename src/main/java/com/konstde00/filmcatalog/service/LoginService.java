package com.konstde00.filmcatalog.service;

import com.konstde00.filmcatalog.model.dto.login.JwtDto;
import com.konstde00.filmcatalog.model.dto.login.LoginByEmailDto;
import com.konstde00.filmcatalog.model.dto.login.LoginByGoogleDto;
import com.konstde00.filmcatalog.model.dto.login.RefreshTokenDto;
import com.konstde00.filmcatalog.model.enums.UserRegistrationType;
import com.konstde00.filmcatalog.model.exception.NotValidException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import static com.konstde00.filmcatalog.model.enums.BusinessLogicException.WRONG_AUTHORIZATION_TYPE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LoginService {

    UserService userService;
    TokenService tokenService;
    GoogleService googleService;

    public JwtDto authorizationByEmailAndPassword(LoginByEmailDto loginDto) {

        log.info("'authorizationByEmailAndPassword' invoked with params - {}", loginDto);

        var user = userService.getByEmail(loginDto.getEmail());
        userService.validateUserRegistrationType(user, UserRegistrationType.EMAIL_AND_PASSWORD,
                WRONG_AUTHORIZATION_TYPE);

        validatePassword(loginDto.getPassword(), user.getPassword());

        var jwtDto = userService.generateTokens(user);

        log.info("'authorizationByEmailAndPassword' returned - {}", jwtDto);

        return jwtDto;
    }

    public JwtDto authorizationByGoogle(LoginByGoogleDto loginDto) {

        log.info("'authorizationByGoogle' invoked");

        var email = googleService.parseToken(loginDto.getToken());
        var user = userService.getByEmail(email);

        var jwtDto = userService.generateTokens(user);

        log.info("'authorizationByGoogle' returned - {}", jwtDto);

        return jwtDto;
    }

    public JwtDto refreshToken(RefreshTokenDto refreshTokenDto) {

        log.info("'refreshToken' invoked");

        var refreshToken = tokenService.refreshToken(refreshTokenDto.getRefreshToken());
        var jwtDto = tokenService.getAccessAndRefreshTokens(refreshToken.getUser(), refreshTokenDto.getRefreshToken());

        log.info("'refreshToken' returned 'Success'");

        return jwtDto;
    }

    public void logout(Long userId, RefreshTokenDto refreshTokenDto) {

        log.info("'logout' invoked for user with id - {}", userId);

        tokenService.revoke(userId, refreshTokenDto.getRefreshToken());

        log.info("'logout' returned 'Success'");
    }

    public static void validatePassword(String password){
        if(password.length() < 4 || password.length() > 25){
            log.warn("Password length is not valid: "+password.length());
            throw new NotValidException(String.format("Password length is not valid, has to be between 4 and 25 symbols. " +
                    "Current length is %d", password.length()));
        }
    }

    private void validatePassword(String inputPassword, String correctPassword) {

        var passwordValidation = BCrypt.checkpw(inputPassword, correctPassword);

        if (!passwordValidation) {
            throw new NotValidException("Incorrect email or password");
        }
    }
}
