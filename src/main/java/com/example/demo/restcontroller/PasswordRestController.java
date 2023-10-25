package com.example.demo.restcontroller;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PasswordRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/resetpw")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String > requestBody){
        String email = requestBody.get("email");
        String newPassword = requestBody.get("newPassword");

        User user = userRepository.findByEmail(email);

        if(user != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);
            return ResponseEntity.ok("success");
        }   else{
            return ResponseEntity.ok("failure");
        }

    }
}