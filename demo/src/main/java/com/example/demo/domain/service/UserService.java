package com.example.demo.domain.service;

import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional(rollbackFor = SQLException.class)
    public void UserUpdate(UserDto dto){
        User user = new User();

        //양말이 어디ㅣㅇㅆ을까?
    }
}
