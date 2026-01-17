package it.lucianofilippucci.tcgarkhive.configuration.security;

import it.lucianofilippucci.tcgarkhive.configuration.security.authentication.CustomWebAuthenticationDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthContext {
    public String getJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getDetails() instanceof CustomWebAuthenticationDetails details) {
            return details.getJwt();
        }
        return null;
    }
}
