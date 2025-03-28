package com.ampersand.groom.global.security.jwt.filter;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.global.security.jwt.service.JwtParserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JwtParserService jwtParserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtParserService.resolveToken(request);
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/v1/auth")
                || uri.equals("/health")
                || pathMatcher.match("/api/v1/members/**/password", uri)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (token != null && jwtParserService.validateAccessToken(token)) {
            String email = jwtParserService.getEmailFromAccessToken(token);
            MemberRole roles = jwtParserService.getRolesFromAccessToken(token);
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roles.name()));
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Unauthorized or invalid token.\"}");
    }
}