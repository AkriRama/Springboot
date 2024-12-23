package com.example.transcations.transactions.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transcations.transactions.model.User;
import com.example.transcations.transactions.model.UserLogin;
import com.example.transcations.transactions.model.dto.ChangePasswordDTO;
import com.example.transcations.transactions.model.dto.LoginDTO;
import com.example.transcations.transactions.model.dto.RegisterDTO;
import com.example.transcations.transactions.model.dto.UserDTO;
import com.example.transcations.transactions.repository.RoleRepository;
import com.example.transcations.transactions.repository.UserLoginRepository;
import com.example.transcations.transactions.repository.UserRepository;
import com.example.transcations.transactions.service.api.UserManagementRestService;
import com.example.transcations.transactions.utils.CustomResponse;
import com.example.transcations.transactions.utils.EmailConfirmation;
import com.example.transcations.transactions.utils.GeneratePassword;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/user-management")
public class UserManagementRestController {
    private UserRepository userRepository;
    private UserLoginRepository userLoginRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private EmailConfirmation emailConfirmation;
    private UserManagementRestService userManagementRestService;

    @Autowired
    public UserManagementRestController(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserLoginRepository userLoginRepository, RoleRepository roleRepository,
            EmailConfirmation emailConfirmation, UserManagementRestService userManagementRestService) {
        this.userRepository = userRepository;
        this.userLoginRepository = userLoginRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailConfirmation = emailConfirmation;
        this.userManagementRestService = userManagementRestService;
    }

    @PostMapping("login")
    public ResponseEntity<Object> index(@RequestBody LoginDTO loginDTO) {
        if (userRepository.getUsingDTO(loginDTO.getEmail()) != null) {
            if (passwordEncoder.matches(loginDTO.getPassword(),
                    userRepository.getUsingDTO(loginDTO.getEmail()).getPassword())) {
                return CustomResponse.generate(HttpStatus.OK, "Login Successfull");
            } else {
                return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Incorrect Password");
            }
        } else {
            return CustomResponse.generate(HttpStatus.OK, "User not found");
        }
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO registerDTO) {
        if (userRepository.getUsingDTO(registerDTO.getEmail()) == null) {
            if (registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
                User user = new User();
                user.setNickname(registerDTO.getNickname());
                user.setFullname(registerDTO.getFullname());
                user.setEmail(registerDTO.getEmail());
                user.setAddress(registerDTO.getAddress());
                user.setTelephone(registerDTO.getTelephone());
                userRepository.save(user);
                UserLogin userLogin = new UserLogin();
                userLogin.setId(user.getId());
                userLogin.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
                userLogin.setRole(roleRepository.getLevel1());
                userLoginRepository.save(userLogin);
                emailConfirmation.confirmation("registration", registerDTO.getEmail(), "");
                if (emailConfirmation.confirmation("registration", registerDTO.getEmail(), "")) {
                    return CustomResponse.generate(HttpStatus.OK, "Registered Successfull. Check Your Email");
                }
                return CustomResponse.generate(HttpStatus.OK, "Registered Successfull. Check Your Email");
            } else {
                return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Password and Confirm Password not match");
            }
        } else {
            return CustomResponse.generate(HttpStatus.BAD_REQUEST, "User with email " +
                    registerDTO.getEmail()
                    + " is already registered. Please use a different email.");
        }
    }

    @PutMapping("reset-password/{email}")
    public ResponseEntity<Object> reset(@PathVariable(required = true) String email) {
        if (userRepository.getUsingDTO(email) != null) {
            String newPassword = GeneratePassword.randomPassword(20);
            UserLogin userLogin = userLoginRepository.get(userRepository.getUsingDTO(email).getId());
            userLogin.setPassword(passwordEncoder.encode(newPassword));
            userLoginRepository.save(userLogin);
            emailConfirmation.confirmation("reset", email, newPassword);
            return CustomResponse.generate(HttpStatus.OK,
                    "Your password has been reset. Please check your email for the new password and make sure to change it immediately.");
        }
        return CustomResponse.generate(HttpStatus.OK,
                "Email not found");
    }

    @PutMapping("change-password/{email}")
    public ResponseEntity<Object> change(@PathVariable(required = true) String email,
            @RequestBody ChangePasswordDTO changePasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (userRepository.getUsingDTO(email) != null) {
                UserDTO userDTO = userRepository.getUsingDTO(email);
                if (passwordEncoder.matches(changePasswordDTO.getOldPassword(),
                        userDTO.getPassword())) {
                    if (changePasswordDTO.getPassword().equals(changePasswordDTO.getConfirmPassword())) {
                        if (!changePasswordDTO.getPassword().equals(changePasswordDTO.getOldPassword())) {
                            UserLogin userLogin = userLoginRepository.findById(userDTO.getId()).get();
                            userLogin.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
                            userLoginRepository.save(userLogin);
                            emailConfirmation.confirmation("change", email,
                                    changePasswordDTO.getPassword());
                            return CustomResponse.generate(HttpStatus.OK,
                                    "Your password has been changed. Please change your email for notification.");
                        } else {
                            return CustomResponse.generate(HttpStatus.BAD_REQUEST,
                                    "The old password and the new password cannot be the same. Please choose a different new password.");
                        }
                    } else {
                        return CustomResponse.generate(HttpStatus.BAD_REQUEST,
                                "New Password and Confirm Password not match.");
                    }
                } else {
                    return CustomResponse.generate(HttpStatus.BAD_REQUEST,
                            "Incorrect Password");
                }
            } else {
                return CustomResponse.generate(HttpStatus.OK,
                        "Email not found.");
            }
        } else {
            return CustomResponse.generate(HttpStatus.UNAUTHORIZED,
                    "You need to log in.");
        }
    }

    @PostMapping("logout")
    public ResponseEntity<Object> logout() {
        SecurityContextHolder.clearContext();
        return CustomResponse.generate(HttpStatus.OK, "You have been logged out successfully");
    }

}
