package com.c2g4.SingHealthWebApp.JWT;

import java.util.ArrayList;
import java.util.List;

import com.c2g4.SingHealthWebApp.JWT.BruteForce.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;

import javax.servlet.http.HttpServletRequest;

@Service
public class AppUserDetailsService implements UserDetailsService{
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private LoginAttemptService loginAttemptService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String s) throws DisabledException, UsernameNotFoundException {
        final String clientIP = extractIPFromClient();
        logger.info("IP ADDRESS {}", clientIP);

        if (loginAttemptService.isBlocked(clientIP)) {
            logger.warn("USER IP {} BLOCKED",clientIP);
            throw new RuntimeException("IP address blocked");
        }

        AccountModel accountModel;
        logger.warn("LOADUSERBYUSERAME");

        accountModel = accountRepo.findByUsername(s);

        if(accountModel == null) {
            logger.warn("USERNAME NOT FOUND IN DATABASE");
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        if(accountModel.isIs_locked()){
            logger.warn("USERNAME LOCKED");
            throw new DisabledException(String.format("Username %s has been locked after multiple login attempts",s));
        }

        logger.warn(String.format("USERNAME %s FOUND IN DATABASE",s));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(accountModel.getRole_id()));
        UserDetails userDetails = new User(accountModel.getUsername(), accountModel.getPassword(), authorities);

        return userDetails;
    }

    private String extractIPFromClient() {
        //X-Forwarded-For: clientIpAddress, proxy1, proxy2
        String xfHeader = httpServletRequest.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return httpServletRequest.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
