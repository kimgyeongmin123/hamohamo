package com.example.demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

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
    private String addr;

    private String role;


    //OAuth2 Added
    private String provider;
    private String providerId;

}