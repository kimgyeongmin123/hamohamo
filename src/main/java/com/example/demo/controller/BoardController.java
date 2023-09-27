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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/update")
    public void update(@RequestParam Long number, Model model){
        log.info("GET /update no : " + number);

        // 게시물 번호로 해당 게시물 정보 가져오기
        Optional<Board> boardOptional = boardRepository.findByNum(number);

        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            BoardDto dto = new BoardDto();
            dto.setNumber(board.getNumber());
            dto.setContents(board.getContents());
            dto.setEmail(board.getEmail());
            dto.setDate(board.getDate());
            dto.setHits(board.getHits());
            dto.setLike_count(board.getLike_count());
            System.out.println("dto : " + dto);

            // 모델에 게시물 정보 전달
            model.addAttribute("boardDto", dto);

        }
    }

    @PostMapping("/update")
    public String postUpdate(@Valid BoardDto dto,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam String newContents) {
        log.info("POST /update number: " + dto.getNumber() + ", newContents: " + newContents);

        if (bindingResult.hasFieldErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info(error.getField() + " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "mypage"; // 폼 다시 표시
        }

        boolean isAdd = boardService.updateBoard(dto.getNumber(), newContents);

        if (isAdd) {
            return "redirect:/mypage";
        }
        return "redirect:/list";

    }
    @DeleteMapping("/delete/{number}")
    public String delete(@PathVariable Long number, RedirectAttributes redirectAttributes) {
        log.info("DELETE /delete/{number} number: " + number);

        // 게시물 번호로 해당 게시물 정보 가져오기
        Optional<Board> boardOptional = boardRepository.findByNum(number);

        if (boardOptional.isPresent()) {
            // 게시물을 데이터베이스에서 삭제
            boardRepository.delete(boardOptional.get());
            redirectAttributes.addFlashAttribute("successMessage", "게시물이 성공적으로 삭제되었습니다.");
        } else {
            // 게시물이 존재하지 않을 경우 예외 처리 (이 부분을 적절히 처리하세요)
            redirectAttributes.addFlashAttribute("errorMessage", "게시물을 찾을 수 없습니다.");
        }

        return "redirect:/mypage"; // 삭제 후 리스트 페이지로 리다이렉트
    }

}