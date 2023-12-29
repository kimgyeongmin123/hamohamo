package com.example.demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private int room;
    private String send_nick;
    private String recv_nick;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 유동적으로 바뀌게
    private LocalDateTime sendTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 유동적으로 바뀌게
    private LocalDateTime readTime;

    // 현재 사용자의 메시지 상대 nickname 담기
    private String other_nick;

    // 현재 사용자의 메시지 상대 프로필을 담기
    private String profile;

    // 현재 사용자의 nickname 담기
    private String nick;

    // 안읽은 메시지 개수
    private int unread;
}