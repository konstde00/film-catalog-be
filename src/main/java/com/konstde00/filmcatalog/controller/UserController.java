package com.konstde00.filmcatalog.controller;

import com.konstde00.filmcatalog.model.dto.login.ConfirmationCodeDto;
import com.konstde00.filmcatalog.model.dto.login.JwtDto;
import com.konstde00.filmcatalog.model.dto.login.PasswordRecoveryDto;
import com.konstde00.filmcatalog.model.dto.login.SimpleResponse;
import com.konstde00.filmcatalog.model.dto.registration.ChangePasswordDto;
import com.konstde00.filmcatalog.model.dto.registration.RegistrationByEmailDto;
import com.konstde00.filmcatalog.model.dto.registration.RegistrationByGoogleDto;
import com.konstde00.filmcatalog.model.dto.user.CheckUsernameDto;
import com.konstde00.filmcatalog.model.dto.user.UserDto;
import com.konstde00.filmcatalog.model.entity.User;
import com.konstde00.filmcatalog.service.FileService;
import com.konstde00.filmcatalog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    FileService fileService;

    @PostMapping("/v1/check/username")
    @Operation(summary = "Check if username already exists")
    public ResponseEntity<Boolean> checkUsername(@RequestBody @Valid CheckUsernameDto checkUsernameDto) {

        log.info("Check if username {} already exists", checkUsernameDto.getUsername());

        var exists = userService.checkUsername(checkUsernameDto);

        log.info("Check username already exists returned - {}", exists);

        return ResponseEntity.ok(exists);
    }

    @GetMapping("/v1")
    @Operation(summary = "Get user by id")
    public ResponseEntity<User> getById(@RequestParam Long userId) {

        log.info("Get user by id {}", userId);

        var user = userService.getById(userId);

        log.info("Get user by id returned - {}", user);

        return ResponseEntity.ok(user);
    }
    
    @PatchMapping("/v1")
    @Operation(summary = "Update user")
    public ResponseEntity<UserDto> update(@RequestParam Long userId,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) String userName) {

        var updatedUser = userService.update(userId, name, userName);

        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/v1/registration/email")
    @Operation(summary = "User registration by email")
    public ResponseEntity<JwtDto> registrationByEmail(@RequestBody @Valid RegistrationByEmailDto registrationDto) {

        log.info("User tries to register by email with params - {}", registrationDto);

        var jwtDto = userService.registrationByEmail(registrationDto);

        log.info("User successfully registered");

        return new ResponseEntity<>(jwtDto, HttpStatus.CREATED);
    }

    @PostMapping("/v1/registration/google")
    @Operation(summary = "User registration by google")
    public JwtDto registrationByGoogle(@RequestBody @Valid RegistrationByGoogleDto registrationDto) {

        log.info("User tries to register by google with params - {}", registrationDto);

        var jwtDto = userService.registrationByGoogle(registrationDto);

        log.info("User successfully registered");

        return jwtDto;
    }

    @PutMapping("/v1/password/change")
    @Operation(summary = "Change password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {

        log.info("User with tries to change password");

        userService.changePassword(changePasswordDto);

        log.info("User changed password successfully.");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/v1/password/recovery")
    @Operation(summary = "Change password")
    public ResponseEntity<SimpleResponse> recoverPassword(@RequestBody PasswordRecoveryDto passwordRecoveryDto) {

        log.info("User with email - {} tries to recover password", passwordRecoveryDto);

        userService.recoverPassword(passwordRecoveryDto);

        log.info("User recover password successfully.");

        return new ResponseEntity<>(new SimpleResponse("Recovery code has been sent successfully"), HttpStatus.OK);
    }

    @PostMapping("/v1/email-code/confirm")
    @Operation(summary = "Confirm email confirmation code")
    public ResponseEntity<JwtDto> confirmEmailCode(@RequestBody @Valid ConfirmationCodeDto confirmCodeDto) {

        log.info("User tries to confirm email code.");

        var jwtDto = userService.confirmEmailCode(confirmCodeDto.getEmail(), confirmCodeDto.getCode());

        log.info("User confirmed code with - {}", jwtDto);

        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }

    @GetMapping("/v1/info")
    @Operation(summary = "Get user info")
    public UserDto getInfo(@AuthenticationPrincipal Long userId) {

        log.info("User with id - {} try to get own info", userId);

        var user = userService.getInfo(userId);

        log.info("Get info returned - {}", user);

        return user;
    }

    @PostMapping("/v1/upload")
    @Operation(summary = "Upload user avatar")
    public ResponseEntity<?> uploadAvatar(HttpServletRequest request,
                                          @RequestParam("photo")  MultipartFile photo) {

        var user = userService.getCurrentUser(request);

        fileService.updateUsersAvatar(user.getId(), photo);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/v1/blocking")
    @Operation(summary = "Block user by id")
    public ResponseEntity<?> blockUserById(@AuthenticationPrincipal Long userId,
                                           @RequestParam boolean isBlocked) {

        log.debug("Blocking process of user with id - {} is started", userId);

        userService.blockUserById(userId, isBlocked);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/v1")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<?> deleteUserById(@AuthenticationPrincipal Long userId) {

        log.debug("Deleting process of device token for user with id - {} is started", userId);

        userService.deleteUserById(userId);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
