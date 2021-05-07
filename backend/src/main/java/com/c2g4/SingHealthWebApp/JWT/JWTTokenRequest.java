package com.c2g4.SingHealthWebApp.JWT;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class JWTTokenRequest implements Serializable {

        private static final long serialVersionUID = -5616176897013108345L;

        private String username;
        private String password;

        public JWTTokenRequest() {
            super();
        }

        public JWTTokenRequest(String username, String password) {
            this.setUsername(username);
            this.setPassword(password);
        }

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

}

