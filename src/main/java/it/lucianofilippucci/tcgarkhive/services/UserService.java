package it.lucianofilippucci.tcgarkhive.services;

import it.lucianofilippucci.tcgarkhive.API.V1.DTO.LoginResponse;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.UserRequest;
import it.lucianofilippucci.tcgarkhive.configuration.security.authentication.JwtUtil;
import it.lucianofilippucci.tcgarkhive.entity.RolesEntity;
import it.lucianofilippucci.tcgarkhive.entity.UserEntity;
import it.lucianofilippucci.tcgarkhive.entity.UserSessionEntity;
import it.lucianofilippucci.tcgarkhive.helpers.ShaHash;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.LoginRequiredException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.UserNotExistsException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.UserSessionNotFoundException;
import it.lucianofilippucci.tcgarkhive.repository.RolesRepository;
import it.lucianofilippucci.tcgarkhive.repository.UserRepository;
import it.lucianofilippucci.tcgarkhive.repository.UserSessionRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private final RolesRepository rolesRepository;
    private final UserSessionRepository userSessionRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, RolesRepository rolesRepository, UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.rolesRepository = rolesRepository;
        this.userSessionRepository = userSessionRepository;
    }

    @Transactional
    public boolean newUser(UserRequest userRequest) {
        UserEntity user = new UserEntity();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
        user.setActive(true);
        Optional<RolesEntity> role = this.rolesRepository.findByName("USER");
        if (role.isEmpty()) {
            System.out.println("Role not found");
            return false;
        }

        Set<RolesEntity> roles = new HashSet<>();
        roles.add(role.get());
        user.setRoles(roles);
        user = this.userRepository.save(user);

        return true;
    }

    @Transactional
    public LoginResponse performLogin(UserRequest userRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(),
                        userRequest.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        LoginResponse loginResponse = new LoginResponse();
        Optional<UserEntity> tmp = this.userRepository.findByUsername(userRequest.getUsername());
        if (tmp.isEmpty()) throw new UsernameNotFoundException("Username not found");
        UserEntity user = tmp.get();

        UserSessionEntity userSession = new UserSessionEntity();
        userSession.setUser(user);
        userSession.setToken("");
        userSession.setRefreshToken("");
        userSession.setCreatedAt(OffsetDateTime.now());
        userSession.setUpdatedAt(OffsetDateTime.now());
        userSession.setRevoked(false);
        UserSessionEntity sessionEntity = this.userSessionRepository.save(userSession);


        String token = jwtUtil.generateAccessToken(user, sessionEntity.getSessionId());
        String refreshToken = this.jwtUtil.generateRefreshToken(user, sessionEntity.getSessionId());
        String hashedtoken = ShaHash.hash(refreshToken);
        String encoded = this.passwordEncoder.encode(hashedtoken);

        loginResponse.setToken(token);
        loginResponse.setRefreshToken(refreshToken);
        return loginResponse;
    }

    @Transactional
    public LoginResponse refreshToken(String refreshToken) throws UserNotExistsException, LoginRequiredException, UserSessionNotFoundException {
        if (this.jwtUtil.validateRefreshToken(refreshToken)) {
            Optional<UserEntity> tmp = this.userRepository.findByUsername(this.jwtUtil.getRefreshTokenSubject(refreshToken));
            if (tmp.isEmpty()) throw new UserNotExistsException("Username not found");
            UserEntity user = tmp.get();

            Long sessionId = this.jwtUtil.getSessionIdFromRefreshToken(refreshToken);
            Optional<UserSessionEntity> tmp2 = this.userSessionRepository.findById(sessionId);
            if (tmp2.isEmpty()) throw new UserSessionNotFoundException("Session not found");
            UserSessionEntity userSession = tmp2.get();

            String hashedToken = ShaHash.hash(refreshToken);
            if (this.passwordEncoder.matches(hashedToken, userSession.getRefreshToken())) {
                String newRefreshToken = this.jwtUtil.generateRefreshToken(user, sessionId);
                String newAccessToken = this.jwtUtil.generateAccessToken(user, sessionId);

                String newHashedToken = ShaHash.hash(newRefreshToken);
                String newEncoded = this.passwordEncoder.encode(newRefreshToken);

                userSession.setRefreshToken(newEncoded);
                userSession.setToken(newAccessToken);
                userSession.setUpdatedAt(OffsetDateTime.now());
                UserSessionEntity sessionEntity = this.userSessionRepository.save(userSession);

                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setToken(newAccessToken);
                loginResponse.setRefreshToken(newRefreshToken);
                return loginResponse;
            }
        }
        throw new LoginRequiredException("Invalid refresh token");
    }

    @Transactional
    public boolean invalidateSession(Long sessionId) {
        Optional<UserSessionEntity> tmp = this.userSessionRepository.findBySessionId(sessionId);
        if(tmp.isEmpty()) return false;
        UserSessionEntity userSession = tmp.get();
        userSession.setUpdatedAt(OffsetDateTime.now());
        userSession.setRevoked(true);
        this.userSessionRepository.save(userSession);
        return true;
    }

    @Transactional
    public boolean CheckUsernameExists(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Transactional
    public boolean CheckEmailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }
}
