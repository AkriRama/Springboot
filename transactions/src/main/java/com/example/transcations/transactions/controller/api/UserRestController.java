package com.example.transcations.transactions.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transcations.transactions.model.User;
import com.example.transcations.transactions.model.UserLogin;
import com.example.transcations.transactions.model.dto.SwitchOnOffDTO;
import com.example.transcations.transactions.model.dto.UserDTO;
import com.example.transcations.transactions.model.dto.UserSaveDTO;
import com.example.transcations.transactions.repository.RoleRepository;
import com.example.transcations.transactions.repository.UserLoginRepository;
import com.example.transcations.transactions.repository.UserRepository;
import com.example.transcations.transactions.utils.CustomResponse;
import com.example.transcations.transactions.utils.GeneratePassword;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/user")
public class UserRestController {
    private UserRepository userRepository;
    private UserLoginRepository userLoginRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserRestController(UserRepository userRepository, UserLoginRepository userLoginRepository,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userLoginRepository = userLoginRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/info")
    public User getUserDetails() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(email).get();
    }

    @GetMapping(value = { "", "/{id}" })
    public ResponseEntity<Object> getData(@PathVariable(required = false) Integer id) {
        if (id != null) {
            UserDTO userDTO = userRepository.getByIDDTO(id);
            return CustomResponse.generate(HttpStatus.OK, "Data Found", userDTO);
        } else {
            List<UserDTO> userDTOs = userRepository.getALLDTO();
            return CustomResponse.generate(HttpStatus.OK, "Data Found", userDTOs);
        }
    }

    @PutMapping("switch/{id}")
    public ResponseEntity<Object> switchOnOff(@PathVariable(required = true) Integer id,
            @RequestBody SwitchOnOffDTO sDto) {
        User user = userRepository.get(id);
        user.setActive(sDto.isActive());
        userRepository.save(user);
        return CustomResponse.generate(HttpStatus.OK, sDto.isActive() ? "User is Active" : "User is not Active");
    }

    @PostMapping(value = { "save", "save/{id}" })
    public ResponseEntity<Object> save(@PathVariable(required = false) Integer id, @RequestBody UserSaveDTO userDTO) {
        User user = new User();
        if (id != null) {
            user = userRepository.get(id);
        }
        user.setFullname(userDTO.getFullname());
        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setActive(userDTO.isActive());
        userRepository.save(user);

        UserLogin userLogin = new UserLogin();
        userLogin.setId(user.getId());
        userLogin.setPassword(passwordEncoder.encode(GeneratePassword.randomPassword(10)));
        userLogin.setRole(roleRepository.getLevelById(userDTO.getRole()));
        userLoginRepository.save(userLogin);
        return CustomResponse.generate(HttpStatus.OK, "Inserted Data Successfully");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        userRepository.delete(id);
        userLoginRepository.delete(id);
        return CustomResponse.generate(HttpStatus.OK, "Deleted Data Successfully");
    }

}
