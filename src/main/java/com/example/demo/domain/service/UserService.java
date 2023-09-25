package com.example.demo.domain.service;

import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional(rollbackFor = SQLException.class)
    public Boolean UserUpdate(String email, String newNickname){
        try {
            User user = userRepository.findByEmail(email);

            if (user != null) {
                user.setNickname(newNickname);
                userRepository.save(user);
                System.out.println("user: " +user);
                return true;
            } else {
                return false; // 사용자를 찾지 못한 경우
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 업데이트 중 예외 발생
        }

    }
}