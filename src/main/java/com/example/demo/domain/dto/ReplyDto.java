package com.example.demo.domain.dto;


import com.example.demo.domain.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {

    private Long bno;
    private Long rnumber;   // 댓글의 번호 빠른거부터 위에 있어야하므로 존재는하지만 표시는 하지않음
    private String nickname; // 댓글쓴사람의 닉네임
    private String content; // contents와 다른 댓글의 내용
    private LocalDateTime date;
}