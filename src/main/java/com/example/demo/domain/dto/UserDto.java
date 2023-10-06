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

	public static UserDto EntityToDto(User user)
	{
		UserDto dto  = new UserDto();
		dto.setEmail(user.getEmail());
		dto.setPassword(user.getPassword());
		dto.setZipcode(user.getZipcode());
		dto.setNickname(user.getNickname());
		dto.setName(user.getName());
		dto.setAddr1(user.getAddr1());
		dto.setAddr2(user.getAddr2());
		dto.setBirth(user.getBirth());
		dto.setPhone(user.getPhone());
		dto.setRole(user.getRole());
		dto.setProfile(user.getProfile());
		dto.setAnswer(user.getAnswer());
		dto.setQuestion(user.getQuestion());

		return dto;

	}

}