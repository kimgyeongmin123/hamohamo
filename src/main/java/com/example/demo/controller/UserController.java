package com.example.demo.controller;

import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.BoardRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.domain.service.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class UserController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private BoardRepository boardRepository;

	@GetMapping("/join")
	public void join_get() {
		log.info("GET /join");
	}

	@PostMapping("/join")
	public String join_post(UserDto dto) {
		log.info("POST /join "+dto);

		dto.setRole("ROLE_USER");
		dto.setPassword( passwordEncoder.encode(dto.getPassword()) );

		User user = new User();

		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setNickname(dto.getNickname());
		user.setName(dto.getName());
		user.setZipcode(dto.getZipcode());
		user.setAddr1(dto.getAddr1());
		user.setAddr2(dto.getAddr2());
		user.setBirth(dto.getBirth());
		user.setPhone(dto.getPhone());
		user.setQuestion(dto.getQuestion());
		user.setAnswer(dto.getAnswer());
		user.setProvider(dto.getProvider());
		user.setProviderId(dto.getProviderId());
		user.setProfile("/images/basic_profile.png");
		user.setRole(dto.getRole());

		userRepository.save(user);

		System.out.println("addr1 :" + user.getAddr1());
		System.out.println("addr2 : " + user.getAddr2());
		System.out.println("zipcode :" + user.getZipcode());
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
			dto.setZipcode(user.getZipcode());
			dto.setAddr1(user.getAddr1());
			dto.setAddr2(user.getAddr2());
		}

		model.addAttribute("dto", dto);

		return "profile/update";
	}

	@PostMapping("/profile/update")
	public String UserUpdate(@RequestParam("newNickname") String newNickname,
							 RedirectAttributes redirectAttributes,
							 Model model) {
		log.info("UserUpdate POST/ post");

		// 현재 인증된 사용자의 이메일 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		boolean isUpdate = userService.UserUpdate(email,newNickname);

		if (isUpdate) {
			redirectAttributes.addFlashAttribute("successMessage", "Nickname updated successfully.");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to update nickname.");
		}

		return "redirect:update";
	}

	@GetMapping("/mypage")
	public void showMypage(Model model) {
		// 현재 인증된 사용자의 이메일 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();


		// UserRepository를 사용하여 사용자 정보 가져오기
		User user = userRepository.findById(email).get();

		// UserDto 객체 생성
		UserDto dto = UserDto.EntityToDto(user);

		// 사용자 정보에서 닉네임을 가져와서 설정
		if (user != null) {
			dto.setNickname(user.getNickname());
		}
		System.out.println("MYPAGE : " + dto);
		System.out.println("user.getEmail(): "+user.getEmail() );
		List<Board> myBoards = boardRepository.getBoardByEmailOrderByDateDesc(user.getEmail());


		model.addAttribute("dto", dto);
		model.addAttribute("myBoards", myBoards);
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
	@GetMapping("/user/withdraw")
	public String withdrawUser(Model model, Principal principal, HttpServletRequest request) {
		String email = principal.getName(); // 현재 인증된 사용자의 이메일 가져오기
		String password = request.getParameter("password"); // 사용자 입력에서 비밀번호 가져오기

		boolean isWithdrawn = userService.withdrawUser(email, password);

		if (isWithdrawn) {
			// 회원 탈퇴에 성공한 경우, 로그아웃 처리 및 세션 무효화
			SecurityContextHolder.clearContext(); // 현재 사용자의 보안 컨텍스트를 지웁니다.

			return "redirect:/login?message=WithdrawnSuccessfully";
		} else {
			// 회원 탈퇴에 실패한 경우 에러 메시지 등을 처리합니다.
			return "redirect:/mypage?error=WithdrawFailed";

		}
	}



	@PostMapping("/user/withdraw")
	public String withdrawUserPost(@RequestParam String password, RedirectAttributes redirectAttributes) {
		// 현재 인증된 사용자의 이메일 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		boolean isWithdrawn = userService.withdrawUser(email, password);
		if (isWithdrawn) {
			// 회원 탈퇴 성공 시 로그아웃 및 리다이렉션
			SecurityContextHolder.getContext().setAuthentication(null);
			return "redirect:/login?msg=withdrawn";
		} else {
			// 회원 탈퇴 실패 시 오류 메시지 표시
			redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
			return "redirect:/user/withdraw";
		}
	}


}