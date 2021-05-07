package com.c2g4.SingHealthWebApp.JWT;
import java.io.Serializable;

public class JWTTokenResponse implements Serializable {

    private static final long serialVersionUID = 8317676219297719109L;

    private final String token;
    private final String accountType;

    public JWTTokenResponse(String token,String accountType) {
        this.token = token;
        this.accountType = accountType;
    }

    public String getToken() {
        return this.token;
    }

    public String getAccountType() {
        return accountType;
    }
}