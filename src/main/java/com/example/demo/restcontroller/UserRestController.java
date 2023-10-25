package com.example.demo.restcontroller;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.Getter;
import lombok.Setter;


@RestController
public class UserRestController {
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/findid")
    public ResponseEntity<String> findId(@RequestBody FindRequest findIdRequest) {

        String name = findIdRequest.getName();
        String question = findIdRequest.getQuestion();
        String answer = findIdRequest.getAnswer();
        String phone = findIdRequest.getPhone();

        User foundUser = userRepository.findBynameAndPhoneAndQuestionAndAnswer(name, phone, question, answer);


        if(foundUser != null){
            return ResponseEntity.ok(foundUser.getEmail());
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
    @Getter
    @Setter
    public static class FindRequest {

        private String phone ,name, question, answer;
    }
}