package pl.wojciechgunia.wgapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.wojciechgunia.wgapi.configuration.CustomUserDetailsService;
import pl.wojciechgunia.wgapi.service.CookieService;
import pl.wojciechgunia.wgapi.service.JwtService;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final CookieService cookieService;
    private final RouteValidator routeValidator;

    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String refresh = null;

        if (request.getCookies() != null) {
            for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("Authorization")) {
                    token = value.getValue();
                } else if (value.getName().equals("Refresh")) {
                    refresh = value.getValue();
                }
            }
        }

        if (token != null) {
            authenticateUser(token, request);
        } else if (refresh != null) {
            refreshAuthentication(refresh, request, response);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !routeValidator.hasRole(request, authentication.getAuthorities())) {
            log.warn("User does not have access to the resource: {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied to resource");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String token, HttpServletRequest request) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtService.extractUsername(token));
        if (jwtService.validateToken(token, userDetails)) {
            setAuthentication(userDetails, request);
        }
    }

    private void refreshAuthentication(String refresh, HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtService.extractUsername(refresh));
        if (jwtService.validateToken(refresh, userDetails)) {
            setAuthentication(userDetails, request);

            Cookie refreshCookie = cookieService.generateCookie("Refresh", jwtService.refreshToken(refresh, refreshExp), refreshExp);
            Cookie authCookie = cookieService.generateCookie("Authorization", jwtService.refreshToken(refresh, exp), exp);
            response.addCookie(authCookie);
            response.addCookie(refreshCookie);
        }
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
