package com.example.demo.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Builder
@Table(name = "BoardNotification")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardNotification {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long nid;
    private Long bid;
    @Column
    private String writenickname; //글쓴사람
    @Column
    private String replynickname; //댓글 단 사람
    @Column

    private String message;

    //읽었는지 여부 판단 true=읽었음
    private boolean isread;

    private LocalDateTime rdate;



}