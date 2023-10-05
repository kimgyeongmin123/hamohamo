package com.example.demo.domain.dto;

import com.example.demo.domain.entity.User;
import lombok.Data;

@Data
public class UserDto {


	private String email;
	private String password;

	private String nickname;
	private String name;
	private String zipcode;
	private String addr1;
	private String addr2;
	private String birth;
	private String phone;

	private String role;


	//OAUTH2
	private String provider;
	private String providerId;

	private String profile;

	public static UserDto EntityToDto(User user)
	{
		UserDto dto  = new UserDto();
		dto.setEmail(user.getEmail());
		dto.setProfile(user.getProfile());

		return dto;

	}


}