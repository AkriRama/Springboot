package com.example.transcations.transactions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String fullname;
    private String nickname;
    private String email;
    private String role;
    private String password;

}
