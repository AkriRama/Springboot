package com.example.transcations.transactions.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class GeneratePassword {
    public static String randomPassword(int lengthPassword) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        return RandomStringUtils.random(lengthPassword, characters);
    }
}
