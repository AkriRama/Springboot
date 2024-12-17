package com.example.transcations.transactions.utils;

import java.security.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.transcations.transactions.model.dto.UserDTO;
import com.example.transcations.transactions.repository.UserRepository;

@Component
public class EmailConfirmation {
    private UserRepository userRepository;
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailConfirmation(UserRepository userRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    // ADDITION METHOD FOR SEND TO EMAIL AFTER REGISTRATION
    public boolean confirmation(String type, String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();

        UserDTO user = userRepository.getUsingDTO(email);
        message.setTo(email);
        if (type.equalsIgnoreCase("registration")) {
            message.setSubject("Registration Successfull" + Timestamp.class);
            message.setText("Hi, " + user.getNickname() + "\nRegistration was successfull!");
        } else {
            message.setSubject(type + " Password Successfull");
            String text = "Hi, " + user.getNickname() + "\n" + type
                    + " password was successfull!\nHere is your new password : " + password;
            if (type.equalsIgnoreCase("reset")) {
                text += "\nChange your password immidiately from Profile Page.";
            }
            message.setText(text);

        }
        javaMailSender.send(message);
        return true;
    }
}
