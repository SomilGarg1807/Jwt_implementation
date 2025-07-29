package com.tccs.jwt.service;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public boolean validateUser(String username, String password){
        return "admin".equals(username) && "admin".equals(password);
    }
}
