package com.example.demo.controller;

import com.example.demo.domain.dto.BoardDto;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.repository.BoardRepository;
import com.example.demo.domain.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/list")
    public List<Board> list(Model model, Authentication authentication){
        log.info("GET /list");

        System.out.println("authentication : " + authentication);
        System.out.println("name : " + authentication.getName());
        System.out.println("principal : " + authentication.getPrincipal());
        System.out.println("authorities : " + authentication.getAuthorities());
        System.out.println("detail : " + authentication.getDetails());
        System.out.println("credential : " + authentication.getCredentials());

        List<Board> list = boardRepository.findAll();
        model.addAttribute("board", list);

        return list;
    }

    @GetMapping("/post")
    public void post_get(){
        log.info("GET /post");
    }

    @PostMapping("/post")
    public String post_post(@Valid BoardDto dto, BindingResult bindingResult, Model model) throws IOException {
        log.info("POST /post");

        if(bindingResult.hasFieldErrors()) {
            for( FieldError error  : bindingResult.getFieldErrors()) {
                log.info(error.getField()+ " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/post";
        }

        boolean isadd = boardService.addBoard(dto);
        if(isadd) {
            return "redirect:/list";
        }
        return "redirect:/post";
    }


}
