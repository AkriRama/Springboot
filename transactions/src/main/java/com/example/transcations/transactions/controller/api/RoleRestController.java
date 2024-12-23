package com.example.transcations.transactions.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transcations.transactions.model.Role;
import com.example.transcations.transactions.repository.RoleRepository;
import com.example.transcations.transactions.utils.CustomResponse;

@RestController
@RequestMapping("api")
public class RoleRestController {
    private RoleRepository roleRepository;

    @Autowired
    public RoleRestController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("role")
    public ResponseEntity<Object> get() {
        List<Role> role = roleRepository.findAll();
        if (role != null) {
            return CustomResponse.generate(HttpStatus.OK, "Data is available", role);
        } else {
            return CustomResponse.generate(HttpStatus.OK, "Data is not avalable");
        }
    }

    @GetMapping("role/{id}")
    public ResponseEntity<Object> getId(@PathVariable(required = true) Integer id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            return CustomResponse.generate(HttpStatus.OK, "Data is available", role);
        } else {
            return CustomResponse.generate(HttpStatus.OK, "Data is not avalable");
        }
    }

    @PostMapping("role")
    public Role post(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    @DeleteMapping("role/{id}")
    public ResponseEntity<Object> delete(@PathVariable(required = true) Integer id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            roleRepository.deleteById(id);
            return CustomResponse.generate(HttpStatus.OK, "Deleted Data Successfull");
        } else {
            return CustomResponse.generate(HttpStatus.OK, "Data not found");
        }
    }
}
