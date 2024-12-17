package com.example.transcations.transactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.transcations.transactions.model.User;
import com.example.transcations.transactions.model.dto.UserDTO;
import com.example.transcations.transactions.repository.RoleRepository;
import com.example.transcations.transactions.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("user")
public class UserController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String index(Model model) {
        UserDTO userDTO = userRepository.getUsingDTO(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("user", userDTO);
        return "user/profile";
    }

    // @GetMapping(value = { "form", "form/{id}" })
    // public String form(@PathVariable(required = false) Integer id, Model model) {
    // model.addAttribute("roles", roleRepository.get());
    // if (id != null) {
    // model.addAttribute("user", userRepository.get(id));
    // } else {
    // model.addAttribute("user", new User());
    // // return "user/form";
    // }
    // return "user/edit";
    // }

    // @PostMapping("save")
    // public String save(User user) {
    // userRepository.save(user);
    // return "redirect:/user";
    // }

    // @PostMapping("delete/{id}")
    // public String delete(@PathVariable Integer id) {
    // userRepository.delete(id);
    // return "redirect:/user";
    // }

}
