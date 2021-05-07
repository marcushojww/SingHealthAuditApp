package com.c2g4.SingHealthWebApp.JWT.BruteForce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private LoginAttemptService loginAttemptService;
    @Value("is_dev")
    String isDev;

    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        System.out.println(e.getAuthentication());
        HttpServletRequest httpServletRequest = (HttpServletRequest)e.getAuthentication().getDetails();
        loginAttemptService.clearIPFromCache(httpServletRequest.getRemoteAddr());
    }
}