package com.example.demo.domain.dto;

import com.example.demo.domain.entity.User;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {


	private String email;

	private String password;
	private String repassword;

	private String nickname;
	private String name;
	private String zipcode;
	private String addr1;
	private String addr2;
	private String birth;
	private String phone;
	private String role;
	private String question;    // id, pw 찾을때 찾는방법 회원가입할때 여러질문 중 고르기
	private String answer;  // id , pw 찾을때 찾는방법 회원가입할때 여러질문 중 고른 질문에 대한 답


	//OAUTH2
	private String provider;
	private String providerId;

	private String profile;

	public static User dtoToEntity(UserDto dto)
	{
		User user = User.builder()
				.email(dto.getEmail())
				.nickname(dto.getNickname())
				.password(dto.getPassword())
				.name(dto.getName())
				.zipcode(dto.getZipcode())
				.addr1(dto.getAddr1())
				.addr2(dto.getAddr2())
				.birth(dto.getBirth())
				.phone(dto.getPhone())
				.role(dto.getRole())
				.question(dto.getQuestion())
				.answer(dto.getAnswer())
				.profile(dto.getProfile())
				.build();

		return user;

	}

}