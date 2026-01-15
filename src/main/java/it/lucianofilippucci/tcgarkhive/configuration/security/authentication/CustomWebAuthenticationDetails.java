package it.lucianofilippucci.tcgarkhive.configuration.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private final String jwt;

    public CustomWebAuthenticationDetails(HttpServletRequest request, String jwt) {
        super(request);
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
