package com.example.demo.domain.dto;

import com.example.demo.domain.entity.User;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {

	@NotBlank(message = "ID를 입력하세요.")
	@Email(message = "올바른 이메일 주소를 입력하세요")
	private String email;

	@NotBlank(message = "password를 입력하세요")
//	-----------------------------패스워드 패턴은 일단 주석처리할게 우리 코드 짠 거 확인할려면 계속 로그인 해야되는데 1234가 빠르고 편해서 이거 나중에 완성되고 설정하자-------------------------------
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
	private String password;
	private String repassword;
	@AssertTrue(message = "비밀번호가 같지 않습니다")
	private boolean isValid(){
		return password.equals(repassword);
	}

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
		dto.setProfile(user.getProfile());

		return dto;

	}

}