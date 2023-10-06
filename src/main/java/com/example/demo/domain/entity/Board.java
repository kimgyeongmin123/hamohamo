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
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 전략 사용
    private Long number; // 또는 다른 타입을 사용할 수 있음
    private String email;
    private String contents;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 유동적으로 바뀌게
    private LocalDateTime date;
    private Long hits;
    private Long like_count;
    private String dirpath;
    private String filename;
    private String filesize;
}