package com.example.transcations.transactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.transcations.transactions.model.Role;
import com.example.transcations.transactions.repository.RoleRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("role")
public class RoleController {
    private RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // localhost:8080/role/form -> create & update
    // @GetMapping("form")
    // public String form(Model model) {
    // Role role = new Role();
    // role.setId(roleRepository.findAll().get(roleRepository.findAll().size() -
    // 1).getId() + 1);
    // model.addAttribute("role", role);
    // return "role/form";
    // }
    @GetMapping(value = { "form", "form/{id}" })
    public String form(@PathVariable(required = false) Integer id, Model model) {
        if (id != null) {
            Role role = roleRepository.findById(id).get();
            model.addAttribute("role", role);
            return "role/edit";
        } else {
            Role role = new Role();
            role.setId(roleRepository.findAll().get(roleRepository.findAll().size() - 1).getId() + 1);
            model.addAttribute("role", role);
            return "role/form";
        }
    }

    // localhost:8080/role/form -> save
    @PostMapping("save")
    public String save(Role role) {
        roleRepository.save(role);
        return "redirect:/role";
    }

    // localhost:8080/role/delete/{id} -> delete
    // @PostMapping("delete/{id}")
    @PostMapping("delete/{id}")
    public String delete(@PathVariable Integer id) {
        roleRepository.deleteById(id);
        return "redirect:/role";
    }

    // localhost:8080/role/edit/{id} -> edit
    @PostMapping("edit/{id}")
    public String edit(Role role) {
        Role editRole = roleRepository.findById(role.getId()).get();
        editRole.setName(role.getName());
        roleRepository.save(editRole);
        return "redirect:/role";
    }

}
