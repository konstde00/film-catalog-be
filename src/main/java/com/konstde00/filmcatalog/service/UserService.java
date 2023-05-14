package com.konstde00.filmcatalog.service;

import com.konstde00.filmcatalog.mapper.UserMapper;
import com.konstde00.filmcatalog.model.dto.login.JwtDto;
import com.konstde00.filmcatalog.model.dto.login.PasswordRecoveryDto;
import com.konstde00.filmcatalog.model.dto.registration.ChangePasswordDto;
import com.konstde00.filmcatalog.model.dto.registration.RegistrationByEmailDto;
import com.konstde00.filmcatalog.model.dto.registration.RegistrationByGoogleDto;
import com.konstde00.filmcatalog.model.dto.user.CheckUsernameDto;
import com.konstde00.filmcatalog.model.dto.user.UserDto;
import com.konstde00.filmcatalog.model.entity.ConfirmationCode;
import com.konstde00.filmcatalog.model.entity.User;
import com.konstde00.filmcatalog.model.enums.BusinessLogicException;
import com.konstde00.filmcatalog.model.enums.UserRegistrationType;
import com.konstde00.filmcatalog.model.exception.AlreadyExistException;
import com.konstde00.filmcatalog.model.exception.ExpiredException;
import com.konstde00.filmcatalog.model.exception.NotValidException;
import com.konstde00.filmcatalog.model.exception.ResourceNotFoundException;
import com.konstde00.filmcatalog.repository.ConfirmationCodeRepository;
import com.konstde00.filmcatalog.repository.UserRepository;
import com.konstde00.filmcatalog.util.DateUtil;
import com.konstde00.filmcatalog.util.PasswordValidator;
import com.konstde00.filmcatalog.util.SecureRandomUtil;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.konstde00.filmcatalog.model.enums.TokenType.ACCESS;
import static com.konstde00.filmcatalog.model.enums.UserRegistrationType.*;
import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    final UserMapper userMapper;
    final TokenService tokenService;
    final EmailService emailService;
    final GoogleService googleService;
    final UserRepository userRepository;
    final ConfirmationCodeRepository confirmationCodeRepository;

    @Value("${jwt.secret}")
    String jwtSecret;

    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new NotValidException("Not found a user with id " + id));
    }

    public User getByEmail(String email) {

        log.info("'getByEmail' invoked with email - {}", email);

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error(format("User with email - %s does not exist. ", email));
                    return new ResourceNotFoundException(format("User with email - %s does not exist. ", email));
                });

        log.info("'getByEmail' returned 'Success'");

        return user;
    }

    @Transactional(readOnly = true)
    public Boolean checkUsername(CheckUsernameDto checkUsernameDto) {

        log.info("'checkUsername' invoked with params - {}", checkUsernameDto);

        var exists = userRepository.existsByUsername(checkUsernameDto.getUsername());

        log.info("'checkUsername' returned - {}", exists);

        return exists;
    }

    public void validateUserRegistrationType(User user, UserRegistrationType requiredType, BusinessLogicException exceptionType) {
        if (!user.getRegistrationType().equals(requiredType)) {
            log.warn("Wrong type of authorization - " + user.getRegistrationType());
            throw new NotValidException(exceptionType.name());
        }
    }

    public UserDto update(Long userId, String name, String userName) {

        log.info("'updateUser' invoked with params - {}, {}, {}", userId, name, userName);

        var user = getById(userId);
        if (name != null) {
            user.setName(name);
        }
        if (userName != null) {
            checkUsername(CheckUsernameDto.builder().username(userName).build());
            user.setUsername(userName);
        }

        var updatedUser = userRepository.saveAndFlush(user);

        var userGeneralInfo = userMapper.toGeneralInfoDto(updatedUser);
        var userSettingsDto = userMapper.toSettingsDto(updatedUser);

        return UserDto
                .builder()
                .general(userGeneralInfo)
                .settings(userSettingsDto)
                .build();
    }

    @SneakyThrows
    @Transactional
    public JwtDto registrationByEmail(RegistrationByEmailDto registrationDto) {

        log.info("'registrationByEmail' invoked with params - {}", registrationDto);

        PasswordValidator.validatePassword(registrationDto.getPassword());

        var user = createNew(registrationDto.getEmail(), registrationDto.getPassword(),
                registrationDto.getName(), registrationDto.getUsername(), EMAIL_AND_PASSWORD);

        var jwtDto = generateTokens(user);

        log.info("'registrationByEmail' returned - {}", jwtDto);

        return jwtDto;
    }

    public JwtDto registrationByGoogle(RegistrationByGoogleDto registrationDto) {

        log.info("'registrationByGoogle' invoked with params - {}", registrationDto);

        var email = googleService.parseToken(registrationDto.getToken());

        var user = createNew(email, "", email, email, GOOGLE);

        var jwtDto = generateTokens(user);

        log.info("'registrationByGoogle' returned - {}", jwtDto);

        return jwtDto;
    }

    @Transactional(readOnly = true)
    public UserDto getInfo(Long userId) {

        log.info("'getInfo' invoked with user id - {}", userId);

        var user = getById(userId);

        var userGeneralInfo = userMapper.toGeneralInfoDto(user);
        var userSettingsDto = userMapper.toSettingsDto(user);

        return UserDto
                .builder()
                .general(userGeneralInfo)
                .settings(userSettingsDto)
                .build();
    }

    private String generatePassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        return gen.generatePassword(6, lowerCaseRule,
                upperCaseRule, digitRule);
    }

    public User createNew(String email, String password, String name, String username, UserRegistrationType type) {

        log.info("'createNew' invoked with email - {} and type - {}",
                email, type);

        checkEmailDuplication(email);

        var user = switch (type) {
            case EMAIL_AND_PASSWORD -> userRepository.save(userMapper
                    .toEntity(email, name, username, bcryptPassword(password), type));
            case GOOGLE, FACEBOOK, APPLE -> userRepository.save(userMapper
                    .toEntity(email, name, username, "", type));
        };

        log.info("'createNew' returned - {}", user);

        return user;
    }

    @SneakyThrows
    @Transactional
    public void recoverPassword(PasswordRecoveryDto passwordRecoveryDto) {

        checkEmail(passwordRecoveryDto.getEmail());

        var user = getByEmail(passwordRecoveryDto.getEmail());

        ConfirmationCode code = generateConfirmationCode(user);
        confirmationCodeRepository.save(code);

        emailService.sendRecoveryCode(passwordRecoveryDto.getEmail(), code.getCode());
    }

    private void checkEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotValidException("User with email `" + email + "` does not exist");
        }
    }

    private ConfirmationCode generateConfirmationCode(User user) {
        return ConfirmationCode.builder()
                .user(user)
                .code(generateCode())
                .expiredAt(LocalDateTime.now().plusHours(2))
                .build();
    }

    @Transactional
    public void changePassword(ChangePasswordDto changePasswordDto) {

        var user = getByEmail(changePasswordDto.getEmail());

        LoginService.validatePassword(changePasswordDto.getNewPassword());

        user.setPassword(bcryptPassword(changePasswordDto.getNewPassword()));

        log.info("'changePassword' returned 'Success'");
    }

    @Transactional
    public void deleteUserById(Long userId) {
        // log out by socket

        var user = userRepository.findById(userId)
                .orElseThrow(() -> {

                    var message = format("User with id - %d does not exist.", userId);
                    log.error(message);

                    return new ResourceNotFoundException(message);
                });

        // actually delete user
        userRepository.deleteUserById(user.getId());
    }

    public JwtDto generateTokens(User user) {

        var refreshToken = tokenService.generateRefreshToken(user);

        return tokenService.getAccessAndRefreshTokens(user, refreshToken);
    }

    @Transactional
    public JwtDto confirmEmailCode(String email, int code) {

        log.info("'confirmEmailCode' invoked with email - {}", email);

        var userCodes = confirmationCodeRepository.findAllByEmailAndCode(email, code)
                .stream()
                .filter(uc -> uc.getCode().equals(code))
                .toList();

        if (userCodes.isEmpty()) {

            throw  new ResourceNotFoundException("Incorrect confirmation code");
        }

        userCodes.stream()
                .filter(uc -> {
                    log.info("uc.getExpiredAt() - {}", uc.getExpiredAt());
                    return uc.getExpiredAt().isAfter(DateUtil.getLocalDateTimeNow());
                })
                .findAny()
                .orElseThrow(() -> new ExpiredException("Your code has expired"));

        log.info("'confirmEmailCode' returned - {}", true);

        return generateTokens(userCodes.get(0).getUser());
    }

    public User getCurrentUser(HttpServletRequest request) {

        var token = request.getHeader("Authorization");

        var claims = token.replace("Bearer ", "");

        log.info("claims = {}'", claims);

        var claimsJws = Jwts.parserBuilder()
                .requireIssuer(ACCESS.name())
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(claims);

        var userId = Long.parseLong(claimsJws.getBody().getSubject());

        return getById(userId);
    }

    public void blockUserById(Long userId, Boolean isBlocked) {

       User user = getById(userId);
       user.setIsBlocked(isBlocked);
       userRepository.save(user);
    }

    private int generateCode() {
        return 1000 + SecureRandomUtil.getRandom(9000);
    }

    private void checkEmailDuplication(String email) {

        if (userRepository.existsByEmail(email)) {
            log.error(format("User with email %s already exists. ", email));
            throw new AlreadyExistException(format("User with email %s already exists. ", email));
        }
    }

    private String bcryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
