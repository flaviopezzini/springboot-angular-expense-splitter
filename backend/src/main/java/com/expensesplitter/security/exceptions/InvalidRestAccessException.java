package com.expensesplitter.security.exceptions;

import org.springframework.security.core.AuthenticationException;

import com.expensesplitter.security.model.token.JwtToken;

public class InvalidRestAccessException extends AuthenticationException {
    private static final long serialVersionUID = -5959543783324224864L;
    
    private JwtToken token;

    public InvalidRestAccessException(String msg) {
        super(msg);
    }

    public InvalidRestAccessException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
