//package com.api_task_management.security.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Configuration
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
//    private final SecurityErrorResponseWriter securityErrorResponseWriter;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        System.out.println("Hit this man");
//        String authHeader = request.getHeader("Authorization");
//
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//
//        if (token.isBlank()) { // if header is present then only it will block
////            filterChain.doFilter(request, response);
////            response.sendError(
////                    HttpServletResponse.SC_UNAUTHORIZED,
////                    "Empty Bearer token"
////            );
//
//            securityErrorResponseWriter.writeError(
//                    request,
//                    response,
//                    HttpServletResponse.SC_UNAUTHORIZED,
//                    "Unauthorized",
//                    "Empty Bearer token"
//            );
//
//            return;
//        }
//
//
//        try {
//            Claims claims = jwtService.extractAllClaims(token);
//            String email = claims.getSubject();
//            UserDetails user = userDetailsService.loadUserByUsername(email);
//
//            if (!user.isEnabled()
//                    || !user.isAccountNonLocked()
//                    || !user.isAccountNonExpired()
//                    || !user.isCredentialsNonExpired()) {
//
////                response.sendError(
////                        HttpServletResponse.SC_UNAUTHORIZED,
////                        "User account is not available"
////                );
//                securityErrorResponseWriter.writeError(
//                        request,
//                        response,
//                        HttpServletResponse.SC_UNAUTHORIZED,
//                        "Unauthorized",
//                        "User account is not available"
//                );
//                return;
//            }
//
//            if (email == null || email.isBlank()) {
//                response.sendError(
//                        HttpServletResponse.SC_UNAUTHORIZED,
//                        "Invalid JWT subject"
//                );
//                return;
//            }
//
//            if(!jwtService.isTokenValid(token , user)) {
////                response.sendError(
////                        HttpServletResponse.SC_UNAUTHORIZED,
////                        "Invalid or Expired JWT"
////                );
//                securityErrorResponseWriter.writeError(
//                        request,
//                        response,
//                        HttpServletResponse.SC_UNAUTHORIZED,
//                        "Unauthorized",
//                        "Invalid Token"
//                );
//                return;
//            }
//
//            if(SecurityContextHolder.getContext().getAuthentication() == null) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    user,
//                    null,
//                    user.getAuthorities()
//
//                );
//
//                authentication.setDetails(
//                        new WebAuthenticationDetailsSource()
//                                .buildDetails(request)
//                );
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//
//
//        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException ex) {
////            response.sendError(
////                    HttpServletResponse.SC_UNAUTHORIZED,
////                    "Invalid or expired JWT"
////            );
//            securityErrorResponseWriter.writeError(
//                    request,
//                    response,
//                    HttpServletResponse.SC_UNAUTHORIZED,
//                    "Unauthorized",
//                    "Token Expired"
//            );
//            return;
//        }
//
//
//
//        filterChain.doFilter(request, response);
//
//
//
//    }
//}


package com.api_task_management.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final SecurityErrorResponseWriter errorResponseWriter;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No header: let Spring Security enforce protected-route rules.
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String normalizedHeader = authHeader.trim();

        // "Bearer" with no token value.
        if (normalizedHeader.equalsIgnoreCase("Bearer")) {
            writeUnauthorized(request, response, "Empty Bearer token");
            return;
        }
//
//        // Ignore non-Bearer schemes in this JWT filter.
//        if (!normalizedHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        // Reject unsupported Authorization schemes.
        if (!normalizedHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            errorResponseWriter.writeError(
                    request,
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized",
                    "Unsupported Authorization scheme"
            );
            return;
        }

        String token = normalizedHeader.substring(7).trim();

        // "Bearer " with an empty token value.
        if (token.isEmpty()) {
            writeUnauthorized(request, response, "Empty Bearer token");
            return;
        }

        final Claims claims;

        try {
            // Verifies the JWT signature and validates expiration
            // when the token contains an expiration claim.
            claims = jwtService.extractAllClaims(token);

        } catch (ExpiredJwtException ex) {
            writeUnauthorized(request, response, "Token Expired");
            return;

        } catch (JwtException | IllegalArgumentException ex) {
            writeUnauthorized(request, response, "Invalid Token");
            return;
        }


        String email = claims.getSubject();

        if (email == null || email.isBlank()) {
            writeUnauthorized(request, response, "Invalid JWT subject");
            return;
        }

        // Require an expiration claim as part of this API's token policy.
        Date expiration = claims.getExpiration();

        if (expiration == null) {
            writeUnauthorized(request, response, "Invalid Token");
            return;
        }

        // JJWT checks expiration when parsing a token that has an exp claim.
        // This additional check makes the intended policy explicit.
        if (!expiration.after(new Date())) {
            writeUnauthorized(request, response, "Token Expired");
            return;
        }

        final UserDetails user;


        try {
            user = userDetailsService.loadUserByUsername(email);
            System.out.println(user.getAuthorities() + " authorities");

        } catch (UsernameNotFoundException ex) {
            writeUnauthorized(request, response, "User not found");
            return;
        }

        if (!user.isEnabled()
                || !user.isAccountNonLocked()
                || !user.isAccountNonExpired()
                || !user.isCredentialsNonExpired()) {

            writeUnauthorized(
                    request,
                    response,
                    "Your account is disabled or inactive. Please contact the administrator."
            );
            return;
        }

        if (!email.equals(user.getUsername())) {
            writeUnauthorized(request, response, "Invalid Token");
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(
            HttpServletRequest request,
            HttpServletResponse response,
            String message
    ) throws IOException {

        errorResponseWriter.writeError(
                request,
                response,
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                message
        );
    }
}