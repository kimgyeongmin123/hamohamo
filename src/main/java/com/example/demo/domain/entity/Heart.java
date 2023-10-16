package com.example.demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "email")
    private User user; // 사용자 정보

    @ManyToOne
    @JoinColumn(name = "number")
    private Board board; // 게시물 정보

    // 생성자, getter 및 setter 메서드
}
