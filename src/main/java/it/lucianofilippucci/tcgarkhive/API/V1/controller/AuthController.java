package it.lucianofilippucci.tcgarkhive.API.V1.controller;

import it.lucianofilippucci.tcgarkhive.API.V1.DTO.HttpResponse;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.LoginResponse;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.UserRequest;
import it.lucianofilippucci.tcgarkhive.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/user/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signing")
    public ResponseEntity<HttpResponse<LoginResponse>> authenticateUser(@RequestBody UserRequest userRequest) {
        int statusCode = HttpStatus.OK.value();
        String message = "";
        LoginResponse loginResponse = null;

        try {
            loginResponse = this.userService.performLogin(userRequest);
            return ResponseEntity.ok(
                    HttpResponse.<LoginResponse>builder()
                            .timestamp(LocalDateTime.now())
                            .statusCode(HttpStatus.OK.value())
                            .errorCode(0)
                            .data(loginResponse)
                            .build()
            );
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<HttpResponse<LoginResponse>> registerUser(@RequestBody UserRequest userRequest) {
        if(this.userService.newUser(userRequest)) {
            LoginResponse loginResponse = this.userService.performLogin(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    HttpResponse.<LoginResponse>builder()
                            .timestamp(LocalDateTime.now())
                            .statusCode(HttpStatus.CREATED.value())
                            .errorCode(0)
                            .data(loginResponse)
                    .build()
            );
        }
        return ResponseEntity.badRequest().build();
    }
}
