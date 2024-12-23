package com.example.transcations.transactions.controller.api;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transcations.transactions.model.User;
import com.example.transcations.transactions.model.UserLogin;
import com.example.transcations.transactions.model.dto.LoginDTO;
import com.example.transcations.transactions.model.dto.RegisterDTO;
import com.example.transcations.transactions.repository.RoleRepository;
import com.example.transcations.transactions.repository.UserLoginRepository;
import com.example.transcations.transactions.repository.UserRepository;
import com.example.transcations.transactions.security.JWTUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private UserRepository userRepository;
    private UserLoginRepository userLoginRepository;
    private RoleRepository roleRepository;
    private JWTUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, UserLoginRepository userLoginRepository,
            RoleRepository roleRepository, JWTUtil jwtUtil, AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userLoginRepository = userLoginRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody RegisterDTO registerDTO) {
        String encodedPass = passwordEncoder.encode(registerDTO.getPassword());
        User user = userRepository.findByEmail(registerDTO.getEmail()).get();
        // user.setEmail(registerDTO.getEmail());
        // userRepository.save(user);
        UserLogin userLogin = new UserLogin();
        userLogin.setId(user.getId());
        userLogin.setPassword(encodedPass);
        userLogin.setRole(roleRepository.getLevel1());
        String token = jwtUtil.generateToken(user.getEmail());
        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginDTO loginDTO) {
        try {
            UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword());
            authenticationManager.authenticate(authInputToken);
            String token = jwtUtil.generateToken(loginDTO.getEmail());
            return Collections.singletonMap("jwt-token", token);
        } catch (AuthenticationException authExc) {
            throw new RuntimeException("Invalid Login Credentials");
        }
    }

}
