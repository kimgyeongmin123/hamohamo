package com.example.demo.domain.service;

import com.example.demo.domain.entity.Message;
import com.example.demo.domain.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // 메시지 저장
    public void sendMessage(Message message) {
        // 메시지의 sendTime을 현재 시간으로 설정
        message.setSendTime(LocalDateTime.now());
        // 메시지 저장
        messageRepository.save(message);

        System.out.println(message);
    }

    public List<Message> getMessageList(String nickname, String sendNickname) {
        //내가 보낸 사람의 이메일로 메시지 가져오기
        List<Message> messageBySendEmailList = messageRepository.getMessageBySendEmail(nickname,sendNickname);

        return messageBySendEmailList;
    }
}