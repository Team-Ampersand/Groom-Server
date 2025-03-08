package com.ampersand.groom.global.security.config;

import com.ampersand.groom.domain.auth.application.service.CustomUserDetailsService;
import com.ampersand.groom.domain.auth.application.service.JwtService;
import com.ampersand.groom.domain.auth.expection.UserNotFoundException;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtService.resolveToken(request);

        if (request.getRequestURI().startsWith("/auth") || request.getRequestURI().equals("/health")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (token != null && jwtService.validateToken(token)) {
            String email = jwtService.getEmailFromToken(token);
            MemberRole roles = jwtService.getRoleFromToken(token);
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roles.name()));
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new UserNotFoundException();
        }

        filterChain.doFilter(request, response);
    }

}

