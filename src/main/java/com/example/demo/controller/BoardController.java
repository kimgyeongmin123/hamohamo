package com.example.demo.controller;

import com.example.demo.domain.dto.BoardDto;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.BoardRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.domain.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class BoardController {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public List<Board> list(Model model){
        log.info("GET /list");

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
        }

        model.addAttribute("dto", dto);

        List<Board> list = boardRepository.findAll();
        model.addAttribute("board", list);

        return list;
    }

    @GetMapping("/post")
    public void post_get(){
        log.info("GET /post");
    }

    @PostMapping("/post")
    public String post_post(
            @Valid BoardDto dto,
            BindingResult bindingResult,
            Model model
    ) throws IOException {
        log.info("POST /post");

        if (bindingResult.hasFieldErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info(error.getField() + " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/post"; // 폼 다시 표시
        }

        boolean isAdd = boardService.addBoard(dto);

        if (isAdd) {
            return "redirect:/list";
        }
        return "redirect:/post";
    }
}