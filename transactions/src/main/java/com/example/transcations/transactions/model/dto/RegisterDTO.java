package com.example.transcations.transactions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private String nickname;
    private String fullname;
    private String email;
    private String address;
    private String telephone;
    private String password;
    private String confirmationPassword;
}
