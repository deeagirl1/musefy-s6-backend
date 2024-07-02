package com.example.securityservice.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public void setHttpOnlyCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Enable for HTTPS only
        cookie.setPath("/"); // Set the cookie path
        response.addCookie(cookie);
    }


}
