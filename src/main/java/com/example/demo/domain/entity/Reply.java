package com.example.demo.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rnumber;           //rno댓글번호   bno 게시물 번호
    @ManyToOne
    @JoinColumn(name = "bno",foreignKey = @ForeignKey(name = "FK_reply_board",
            foreignKeyDefinition = "FOREIGN KEY (bno) REFERENCES board(number) ON DELETE CASCADE ON UPDATE CASCADE") ) //FK설정\
    private Board board;
    private String nickname;
    private String content;
    private LocalDateTime date;



}