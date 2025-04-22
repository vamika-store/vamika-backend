package com.vamikastore.vamika.auth.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JWTTokenHelper jwtTokenHelper;

    public JWTAuthenticationFilter(JWTTokenHelper jwtTokenHelper, UserDetailsService userDetailsService) {
        this.jwtTokenHelper = new JWTTokenHelper();
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader); // Debug: Log the Authorization header

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authToken = JWTTokenHelper.getToken(request);
            System.out.println("Extracted Token: " + authToken); // Debug: Log the extracted token

            if (authToken != null) {
                String userName = jwtTokenHelper.getUserNameFromToken(authToken);
                System.out.println("Authenticated User: " + userName); // Debug: Log the username extracted from the token

                if (userName != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    System.out.println("Authorities: " + userDetails.getAuthorities()); // Debug: Log the user's authorities

                    if (jwtTokenHelper.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Set the SecurityContext
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("JWT token validation failed: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
