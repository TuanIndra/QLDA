package com.example.QLDA;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;

public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Retrieve the roles of the authenticated user
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // Redirect based on role
        if (roles.contains("ADMIN")) {
            response.sendRedirect("/admin");
        } else if (roles.contains("USER")) {
            response.sendRedirect("/home");
        }
    }
}