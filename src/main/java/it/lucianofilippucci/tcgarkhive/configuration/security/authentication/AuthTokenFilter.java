package it.lucianofilippucci.tcgarkhive.configuration.security.authentication;

import it.lucianofilippucci.tcgarkhive.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {
        try {
            String jwt = this.parseJwt(req);
            if(jwt != null && this.jwtUtil.validateJwtToken(jwt)) {
                String username = this.jwtUtil.getUsernameFromJwtToken(jwt);
                UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                CustomWebAuthenticationDetails details = new CustomWebAuthenticationDetails(req, jwt);
                authentication.setDetails(details);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.out.println("Cannot set user authentication: " + e.getMessage());
        }
        chain.doFilter(req, res);
    }



    private String parseJwt(HttpServletRequest request) {
        String header= request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
