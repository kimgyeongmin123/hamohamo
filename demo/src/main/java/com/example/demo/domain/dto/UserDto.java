package com.example.demo.domain.dto;

import lombok.Data;

@Data
public class UserDto {


	private String email;
	private String password;

	private String nickname;
	private String name;
	private String addr;
	private String birth;
	private String phone;

	private String role;


	//OAUTH2
	private String provider;
	private String providerId;

}