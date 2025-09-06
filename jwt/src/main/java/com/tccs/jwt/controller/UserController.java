package com.tccs.jwt.controller;

import com.tccs.jwt.dto.LoginRequest;
import com.tccs.jwt.dto.LoginResponse;
import com.tccs.jwt.service.AuthService;
import com.tccs.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest){
        if(authService.validateUser(request.getUsername(), request.getPassword())){
            String token = jwtUtil.generateToken(request.getUsername());

            //store in session
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("jwt",token);
            return ResponseEntity.ok(new LoginResponse(token));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello! You're authenticated");
    }

    //mapping to check whether the token is stored in session or not
    @GetMapping("/session-token")
    public ResponseEntity<?> getSessionToken(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if(session!=null){
            Object jwt = session.getAttribute("jwt");
            if(jwt!=null){
              return ResponseEntity.ok("Stored JWT: "+jwt.toString());
            }
            else{
                return ResponseEntity.ok("No JWT in session");
            }
        }

        return ResponseEntity.ok("No active session");
    }



}
