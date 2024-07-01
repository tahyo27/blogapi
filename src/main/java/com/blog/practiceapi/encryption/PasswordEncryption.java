package com.blog.practiceapi.encryption;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryption {

    private static final SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
            8, 8, 1, 32, 64);

    public String encrypt(String originalPassword) {
        return encoder.encode(originalPassword);
    }

    public boolean matches(String originalPassword, String encryptedPassword) {
        return encoder.matches(originalPassword, encryptedPassword);
    }

}
