package com.example.securityservice.controllers;


import com.example.securityservice.dto.LoginDTO;
import com.example.securityservice.dto.SignUpDTO;
import com.example.securityservice.dto.TokenDTO;
import com.example.securityservice.dto.UserDTO;
import com.example.securityservice.entities.User;
import com.example.securityservice.exception.AuthenticationErrorException;
import com.example.securityservice.security.TokenGenerator;
import com.example.securityservice.services.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserDetailsManager userDetailsManager;
    private final TokenGenerator tokenGenerator;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider refreshTokenAuthProvider;

    private final CookieService cookieService;

    Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(UserDetailsManager userDetailsManager, TokenGenerator tokenGenerator, DaoAuthenticationProvider daoAuthenticationProvider, JwtAuthenticationProvider refreshTokenAuthProvider, CookieService cookieService) {
        this.userDetailsManager = userDetailsManager;
        this.tokenGenerator = tokenGenerator;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.refreshTokenAuthProvider = refreshTokenAuthProvider;
        this.cookieService = cookieService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignUpDTO signupDTO) {
        // Create a new user
        User user = new User(signupDTO.getUsername(), signupDTO.getPassword());
        if(userDetailsManager.userExists(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists. Please choose another one.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.setEmail(signupDTO.getEmail());
        user.setFirstName(signupDTO.getFirstName());
        user.setLastName(signupDTO.getLastName());
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        user.setAuthorities(authorities);
        userDetailsManager.createUser(user);

        // Authenticate the user and generate tokens
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, signupDTO.getPassword(), authorities);
        TokenDTO tokenDTO = tokenGenerator.createToken(authentication);

        return ResponseEntity.status(HttpStatus.OK).body(tokenDTO.getAccessToken());
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        log.info("Received login request for user: {}", loginDTO.getUsername());

        try {
            Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword()));
            log.info("Successfully authenticated user: {}", loginDTO.getUsername());

            TokenDTO token = tokenGenerator.createToken(authentication);
            log.info("Successfully generated token for user: {}", loginDTO.getUsername());

            // Set the token as an HTTP-only cookie
            cookieService.setHttpOnlyCookie(response, "token", token.getAccessToken(), 24 * 60 * 60); // 1 day in seconds

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            throw new AuthenticationErrorException("An error occurred while authenticating user: {}" + loginDTO.getUsername() + e);
        }
    }

    @GetMapping("/validate")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> validate(@RequestParam("token") String token) {
        boolean isValid = tokenGenerator.isAccessTokenValid(token);
        Jwt jwt = tokenGenerator.decodeAccessToken(token);
        if (!isValid) {
            return null;
        }
        return ResponseEntity.ok(new UserDTO(jwt.getSubject(), jwt.getClaim("username")));
    }


    @PostMapping("/token")
    public ResponseEntity<TokenDTO> token(@RequestBody TokenDTO tokenDTO, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
            TokenDTO newToken = tokenGenerator.createToken(authentication);

            // Set the new access token as an HTTP-only secure cookie
            cookieService.setHttpOnlyCookie(response, "token", newToken.getAccessToken(), 24 * 60 * 60); // 1 day in seconds

            return ResponseEntity.ok(newToken);
        } catch (Exception e) {
            // Handle the exception as needed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/sign-out")
    public ResponseEntity<HttpServletResponse> signOut(HttpServletRequest request, HttpServletResponse response) {
        // Delete the access token cookie
        Cookie accessTokenCookie = new Cookie("token", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true); // Set to true for HTTPS-only
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        // Clear the authentication in the current session
        SecurityContextHolder.clearContext();

        // Invalidate the session if applicable
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Return a success response
        return ResponseEntity.ok().build();
    }

}
