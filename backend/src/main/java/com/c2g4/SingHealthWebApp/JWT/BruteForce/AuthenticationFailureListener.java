package com.c2g4.SingHealthWebApp.JWT.BruteForce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private LoginAttemptService loginAService;
    @Value("is_dev")
    String isDev;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        logger.info("FAILED CREDENTIALS GET AUTHENTICATION {}",e.getAuthentication().getDetails());
        System.out.println(e.getAuthentication());
        HttpServletRequest httpServletRequest = (HttpServletRequest)e.getAuthentication().getDetails();
        loginAService.putIPInCache(httpServletRequest.getRemoteAddr());
        logger.info("HTTP SERVLET REMOTE ADDR {}",httpServletRequest.getRemoteAddr());
    }
}