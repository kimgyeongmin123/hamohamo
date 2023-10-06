package com.example.demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {

    @Id
    private String email;
    private String password;

    private String nickname;
    private String name;
    private String phone;
    private String birth;
    private String zipcode;
    private String addr1;
    private String addr2;
    private String role;
    private String profile; //프로필 사진 저장
    private String question;
    private String answer;


    //OAuth2 Added
    private String provider;
    private String providerId;




}