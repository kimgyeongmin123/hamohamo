package com.example.demo.controller;

import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/join")
	public void join_get() {
		log.info("GET /join");
	}

	@PostMapping("/join")
	public String join_post(UserDto dto) {
		log.info("POST /join "+dto);

		//01

		//02

		//03
		dto.setRole("ROLE_USER");
		dto.setPassword( passwordEncoder.encode(dto.getPassword()) );

		User user = new User();

		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());

		user.setNickname(dto.getNickname());
		user.setName(dto.getName());
		user.setAddr(dto.getAddr());
		user.setBirth(dto.getBirth());
		user.setPhone(dto.getPhone());

		user.setProvider(dto.getProvider());
		user.setProviderId(dto.getProviderId());

		user.setRole(dto.getRole());

		userRepository.save(user);

		//04
		return "redirect:login?msg=Join_Success!";

	}

	@GetMapping("/profile/update")
	public String showInfo(Model model) {
		// 현재 인증된 사용자의 이메일 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		// UserDto 객체 생성
		UserDto dto = new UserDto();

		// UserRepository를 사용하여 사용자 정보 가져오기
		User user = userRepository.findByEmail(email);

		// 사용자 정보에서 닉네임을 가져와서 설정
		if (user != null) {
			dto.setNickname(user.getNickname());
			dto.setPassword(user.getPassword());
			dto.setBirth(user.getBirth());
			dto.setPhone(user.getPhone());
			dto.setAddr(user.getAddr());
		}

		model.addAttribute("dto", dto);

		return "profile/update";
	}

	@PostMapping("/profile/update")
	public String UserUpdate(Model model) {
		// 현재 인증된 사용자의 이메일 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		// UserRepository를 사용하여 사용자 정보 가져오기
		User user = userRepository.findByEmail(email);

		// 사용자 정보 업데이트
		if (user != null) {
			user.setNickname(user.getNickname());
			user.setPhone(user.getPhone());
			user.setAddr(user.getAddr());

			// UserRepository를 사용하여 업데이트된 사용자 정보를 저장
			userRepository.save(user);
		}

		return "redirect:update";
	}

	@GetMapping("/mypage")
	public String showMypage(Model model) {
		// 현재 인증된 사용자의 이메일 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		// UserDto 객체 생성
		UserDto dto = new UserDto();

		// UserRepository를 사용하여 사용자 정보 가져오기
		User user = userRepository.findByEmail(email);

		// 사용자 정보에서 닉네임을 가져와서 설정
		if (user != null) {
			dto.setNickname(user.getNickname());
		}

		model.addAttribute("dto", dto);

		return "mypage";
	}
	@GetMapping("/profile/leave_auth")
	public String showauth(Model model) {
		// 현재 인증된 사용자의 이메일 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		// UserDto 객체 생성
		UserDto dto = new UserDto();

		// UserRepository를 사용하여 사용자 정보 가져오기
		User user = userRepository.findByEmail(email);

		// 사용자 정보에서 닉네임을 가져와서 설정
		if (user != null) {
			dto.setNickname(user.getNickname());
		}

		model.addAttribute("dto", dto);

		return "profile/leave_auth";
	}




}