package com.c2g4.SingHealthWebApp.JWT;

import java.util.Calendar;
import java.sql.Date;
import java.util.Objects;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;

@RestController
@CrossOrigin(origins= {"http://localhost:3000","http://localhost:3002" })
public class JWTAuthenticationRestController {

    @Value("${com.c2g4.singHealthAudit.jwt.http.request.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService appUserDetailsService;

    @Autowired
    private AccountRepo accountRepo;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping(value = "${com.c2g4.singHealthAudit.jwt.get.token.uri}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JWTTokenRequest authenticationRequest, HttpServletRequest httpServletRequest)
            throws AuthenticationException {
        logger.info("HTTP SERVLET REQUEST {}",httpServletRequest);
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword(),httpServletRequest);
        logger.warn("AUTHENTICATED");
        accountRepo.changeFailedLoginAndLockAttemptsByUsername(authenticationRequest.getUsername(),0,0,null);
        final UserDetails userDetails = appUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final String accountType = accountRepo.getRoleFromUsername(authenticationRequest.getUsername());
        logger.warn(String.format("Token created %s", token));
        return ResponseEntity.ok(new JWTTokenResponse(token,accountType));
    }

    @RequestMapping(value = "${com.c2g4.singHealthAudit.jwt.refresh.token.uri}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDetails user = appUserDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            String accountType = accountRepo.getRoleFromUsername(user.getUsername());
            return ResponseEntity.ok(new JWTTokenResponse(refreshedToken,accountType));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private void authenticate(String username, String password, HttpServletRequest httpServletRequest) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        logger.debug("in authenticate");

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
            authenticationToken.setDetails(httpServletRequest);
            authenticationManager.authenticate(authenticationToken);
        } catch (InternalAuthenticationServiceException | DisabledException e){
            throw new AuthenticationException("INVALID_CREDENTIALS", e);
        } catch (BadCredentialsException e) {
            if(accountRepo.findByUsername(username)!=null){
                incrementLock(username);
            }
            throw new AuthenticationException("INVALID_CREDENTIALS", e);
        }
//        catch (Exception e){
//            throw new AuthenticationException("UNKNOWN ERROR", e);
//        }
    }

    private void incrementLock(String username){
        AccountModel callerAcc = accountRepo.findByUsername(username);
        boolean isLocked = callerAcc.isIs_locked();
        int attempts = callerAcc.incrementLockAttempts();
        System.out.println("attempts "+attempts);

        if(attempts==5){
            java.util.Date currTime = Calendar.getInstance(TimeZone.getTimeZone("SGT")).getTime();
            logger.info("now {}",currTime);
            accountRepo.changeFailedLoginAndLockAttemptsByUsername(callerAcc.getUsername(),callerAcc.getFailed_login_attempts(),1,currTime);
        } else if(attempts<5){
            accountRepo.changeFailedLoginAndLockAttemptsByUsername(callerAcc.getUsername(),callerAcc.getFailed_login_attempts(),0,null);
        }
    }
}