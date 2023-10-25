package com.example.demo.restcontroller;

import com.example.demo.domain.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class CheckCodeRestController {

    @Autowired
    private EmailService emailService;


    @PostMapping("/checkcode")
    public ResponseEntity<String> checkCode(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {

        String enteredCode = requestBody.get("code");

        HttpSession session = request.getSession();
        String storedCode = (String) session.getAttribute("ePw");

        if (storedCode != null && storedCode.equals(enteredCode)) {
            return ResponseEntity.ok("인증완료");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증실패");
        }

    }
}