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
    private String writenickname;
    @Column
    private String replynickname;
    @Column

    private String message;
    private boolean isread;
    private LocalDateTime rdate;



}