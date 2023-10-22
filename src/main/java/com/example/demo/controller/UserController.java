package com.example.demo.controller;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.BoardRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public String join_post(UserDto dto, Model model, HttpServletRequest request) {
		log.info("POST /join "+dto);

		boolean isjoin = userService.joinMember(dto,model,request);

		if(!isjoin){
			return "login";
		}
		return "redirect:/login?msg=Join_Success!";

	}

	//================================================================
	@GetMapping("/checkDuplicate")
	public void checkDuplicate_get(){
		log.info("GET/checkDuplicate");
	}

	@PostMapping("/checkDuplicate")
	public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestParam("field") String field, @RequestParam("value") String value) {


		boolean isDuplicate = false;

		if ("emailInput".equals(field)) {
			isDuplicate = userRepository.existsByEmail(value);
		}
		Map<String, Boolean> response = new HashMap<>();
		response.put("duplicate", isDuplicate);

		return ResponseEntity.ok(response);
	}
	@GetMapping("/checkNicknameDuplicate")
	public void checkNicknameDuplicate_get(){ log.info("GET/checkNicknameDuplicate");}
	@PostMapping("/checkNicknameDuplicate")
	public ResponseEntity<Map<String, Boolean>> checkNicknameDuplicate(@RequestParam ("field") String field,@RequestParam ("value") String value) {

		boolean isDuplicate = false;

		if("nicknameInput".equals(field)){
			isDuplicate = userRepository.existsByNickname(value);
		}
		Map<String, Boolean> response = new HashMap<>();
		response.put("duplicate", isDuplicate);

		return ResponseEntity.ok(response);
	}
	//================================================================


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
			dto.setName(user.getName());
			dto.setPassword(user.getPassword());
			dto.setBirth(user.getBirth());
			dto.setPhone(user.getPhone());
			dto.setZipcode(user.getZipcode());
			dto.setAddr1(user.getAddr1());
			dto.setAddr2(user.getAddr2());
			dto.setProfile(user.getProfile());
		}

		model.addAttribute("dto", dto);

		return "profile/update";
	}

	@PostMapping("/profile/update")
	public String UserUpdate(@RequestParam("newNickname") String newNickname,
							 @RequestParam("newBirth") String newBirth,
							 @RequestParam("newPhone") String newPhone,
//							 @RequestParam("newZipcode") String newZipcode,
//							 @RequestParam("newAddr1") String newAddr1,
//							 @RequestParam("newAddr2") String newAddr2,
							 RedirectAttributes redirectAttributes,
							 Model model, Authentication authentication) {
		System.out.println("UserUpdate POST/ post");

		// Authentication 의  PrincipalDetails에 변경된 UserDto저장
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		//접속 유저명 받기
		authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();



		boolean isUpdate = userService.UserUpdate(email,newNickname, newBirth, newPhone);

		System.out.println(authentication);



		System.out.println("IsUpdate : "+ isUpdate);
		if (isUpdate) {
//			SecurityContextHolder.getContext().setAuthentication(null);
			redirectAttributes.addFlashAttribute("successMessage", "Nickname updated successfully.");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to update nickname.");
		}

		return "redirect:/login";
	}

	@GetMapping("/mypage")
	public void showMypage(Authentication authentication,Model model){

		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		// 현재 인증된 사용자의 이메일 가져오기
		String email = principalDetails.getUser().getEmail();

		System.out.println("user.getEmail(): "+email );
		List<Board> myBoards = boardRepository.getBoardByEmailOrderByDateDesc(email);
		System.out.println("myBoards' : " + myBoards);

		model.addAttribute("myBoards", myBoards);
		model.addAttribute("userDto",principalDetails.getUser());
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

	@GetMapping("/user/reply/add")
	public ResponseEntity<?> addReply(@RequestParam("bno") Long bno,
									  @RequestParam("content") String content,
									  Model model) {
		// 현재 인증된 사용자의 이메일 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nickname = SecurityContextHolder.getContext().getAuthentication().getName(); // 사용자의 닉네임을 가져옴

		// UserRepository를 사용하여 사용자 정보 가져오기
		User user = userRepository.findByEmail(nickname);

		if (user != null) {
			nickname = user.getNickname();
			// 닉네임을 모델에 추가하여 프론트엔드로 전달
			model.addAttribute("nickname", nickname);
		}
		// 닉네임을 포함한 URL을 생성하여 반환
		String url = String.format("/board/reply/add?bno=%d&content=%s&nickname=%s", bno, content, nickname);

		return ResponseEntity.ok(url);
	}


	@GetMapping("/list/search-nickname")
	public String search(String keyword, Model model){
		List<User> searchList = userService.search_nickname(keyword);
		model.addAttribute("userList",searchList);
		System.out.println("searchList : "+searchList);

//		------------------------------------------------------------
//		// 현재 인증된 사용자의 이메일 가져오기
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String email = authentication.getName();
//
//		// UserRepository를 사용하여 사용자 정보 가져오기
//		User user = userRepository.findById(email).get();
//
//		// UserDto 객체 생성
//		UserDto dto = UserDto.EntityToDto(user);
//		// 사용자 정보에서 닉네임을 가져와서 설정
//		if (user != null) {
//			dto.setNickname(user.getNickname());
//		}
//
//		model.addAttribute("dto", dto);
//		------------------------------------------------------------
		return "search-nickname";
	}


	//프로필이미지 업로드
	@Autowired
	private ResourceLoader resourceLoader;

	private String dirpath = "C:\\hamohamo";

	@Autowired
	private HttpSession httpSession;

	@PostMapping(value="/user/profileimage/upload")
	public @ResponseBody String profileimageUpload(MultipartFile[] file, Authentication authentication) throws IOException {
		log.info("POST  /user/profileimage/upload file : " + file);

		// Authentication 의  PrincipalDetails에 변경된 UserDto저장
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		//접속 유저명 받기
		authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		// UserRepository를 사용하여 사용자 정보 가져오기
		User user = userRepository.findByEmail(email);

		System.out.println("showInfo's user : "+user);

		//저장 폴더 지정
		String uploadPath = dirpath + File.separator + user.getEmail();
		File dir = new File(uploadPath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		else
		{
			//기존 파일 제거
			File[] files = dir.listFiles();
			for(File rmfile : files){
				rmfile.delete();
			}
		}


		System.out.println("--------------------");
		System.out.println("FILE NAME : " + file[0].getOriginalFilename());
		System.out.println("FILE SIZE : " + file[0].getSize() + " Byte");
		System.out.println("--------------------");


		//파일명 추출
		String filename = file[0].getOriginalFilename();
		//파일객체 생성
		File fileobj = new File(uploadPath,filename);
		//업로드
		file[0].transferTo(fileobj);

		user.setProfile("/resources/hamohamo/"+user.getEmail()+"/"+filename);

		//DB에도 넣기
		System.out.println("userDto : "+user);
		userService.updateProfile(user);


		UserDto dto = principalDetails.getUser();
		dto.setProfile("/resources/hamohamo/"+dto.getEmail()+"/"+filename);


		return "ok";

	}


}