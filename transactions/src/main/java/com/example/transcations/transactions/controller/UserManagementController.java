package com.example.transcations.transactions.controller;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.transcations.transactions.model.UserLogin;
import com.example.transcations.transactions.model.dto.ChangePasswordDTO;
import com.example.transcations.transactions.model.dto.LoginDTO;
import com.example.transcations.transactions.model.dto.RegisterDTO;
import com.example.transcations.transactions.model.dto.UserDTO;
import com.example.transcations.transactions.repository.RoleRepository;
import com.example.transcations.transactions.repository.UserLoginRepository;
import com.example.transcations.transactions.repository.UserRepository;
import com.example.transcations.transactions.utils.EmailConfirmation;
import com.example.transcations.transactions.utils.GeneratePassword;

@Controller
@RequestMapping("user-management")
public class UserManagementController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserLoginRepository userLoginRepository;
    private EmailConfirmation emailConfirmation;

    @Autowired
    public UserManagementController(UserRepository userRepository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository, UserLoginRepository userLoginRepository,
            EmailConfirmation emailConfirmation) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userLoginRepository = userLoginRepository;
        this.emailConfirmation = emailConfirmation;
    }

    // LOGIN PROCCESS
    @GetMapping("login")
    public String index(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "user/login";
    }

    @PostMapping("authenticate")
    public String authenticate(RedirectAttributes redirectAttributes, LoginDTO login) {
        UserDTO user = userRepository.getUsingDTO(login.getEmail());
        if (user != null && passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            try {
                User userDetails = new User(
                        user.getId().toString(),
                        "",
                        getAuthorities(user.getRole()));
                PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(
                        // user,
                        user.getEmail(),
                        "",
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Incorrect Password.");
            return "redirect:/user-management/login";
        }
        return "redirect:/user";
    }

    // REGISTER USER
    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("register", new RegisterDTO());
        return "user/register";
    }

    @PostMapping("/register-process")
    public String confirmationRegister(RegisterDTO registerDTO) {
        if (registerDTO != null && registerDTO.getPassword().equals(registerDTO.getConfirmationPassword())) {
            com.example.transcations.transactions.model.User user = new com.example.transcations.transactions.model.User();
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
        }

        return "redirect:/user-management/login";
    }

    // RESET PASSWORD
    @GetMapping("forgot")
    public String forgot(Model model) {
        model.addAttribute("email", "");
        return "user/reset";
    }

    @PostMapping("reset-password")
    public String resetPassword(@RequestParam("email") String email) {
        UserDTO user = userRepository.getUsingDTO(email);
        if (user != null) {
            String newPassword = GeneratePassword.randomPassword(20);

            UserLogin userLogin = userLoginRepository.get(user.getId());
            userLogin.setPassword(passwordEncoder.encode(newPassword));
            userLoginRepository.save(userLogin);
            emailConfirmation.confirmation("reset", user.getEmail(), newPassword);
        }

        return "redirect:/user-management/login";
    }

    // CHANGE PASSWORD
    @GetMapping("change")
    public String changePassword(Model model) {
        model.addAttribute("change", new ChangePasswordDTO());
        return "user/change";
    }

    @PostMapping("change-password")
    public String confirmationPassword(RedirectAttributes redirectAttributes,
            ChangePasswordDTO changePasswordDTO) {
        UserDTO user = userRepository.getUsingDTO(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user != null && passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())
                && changePasswordDTO.getPassword().equals(changePasswordDTO.getConfirmationPassword())) {
            UserLogin userLogin = userLoginRepository.findById(user.getId()).get();

            userLogin.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
            userLoginRepository.save(userLogin);
            emailConfirmation.confirmation("change", user.getEmail(), changePasswordDTO.getPassword());
        } else {
            if (!changePasswordDTO.getPassword().equals(changePasswordDTO.getConfirmationPassword())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Password and confirm Password is not match.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Incorrect Old password.");
            }
            return "redirect:/user-management/change";
        }

        return "redirect:/user";
    }

    // ADDTION METHOD FOR LOGIN AUTHENTICATION (LOGIN)
    private static Collection<? extends GrantedAuthority> getAuthorities(String role) {
        final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
}
