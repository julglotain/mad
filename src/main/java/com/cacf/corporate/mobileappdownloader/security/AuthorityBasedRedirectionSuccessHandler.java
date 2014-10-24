package com.cacf.corporate.mobileappdownloader.security;

import com.cacf.corporate.mobileappdownloader.usermanagement.GrantedAuthorityImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by jug on 22/10/2014.
 */
public class AuthorityBasedRedirectionSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse
            response, Authentication authentication) throws IOException, ServletException {
        if (containsAuthority(authentication.getAuthorities(), GrantedAuthorityImpl.ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/admin");
        } else {
            response.sendRedirect(request.getContextPath() +"/download");
        }

    }

    private boolean containsAuthority(Collection<? extends GrantedAuthority> authorities, GrantedAuthority authorityToCheck) {
        for (GrantedAuthority auth : authorities) {
            if (auth.getAuthority().equals(authorityToCheck.getAuthority())) {
                return true;
            }
        }
        return false;
    }

}
