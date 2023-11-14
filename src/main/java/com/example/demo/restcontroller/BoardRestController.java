package com.example.demo.restcontroller;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.domain.dto.ReplyDto;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.repository.ReplyRepository;
import com.example.demo.domain.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardRestController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private ReplyRepository replyRepository;

    //-------------------
    //댓글추가
    //-------------------
    @GetMapping("/reply/add")
    public void addReply(Long bno, String content , String nickname, String profileimage){
        log.info("GET /reply/add " + bno + " " + content + " " + nickname + " " + profileimage);
        boardService.addReply(bno,content, nickname, profileimage);
    }
    //-------------------
    //댓글 조회
    //-------------------
    @GetMapping("/reply/list")
    public List<ReplyDto> getListReply(Long bno, Model model){
        log.info("GET /reply/list " + bno);
        List<ReplyDto> list =  boardService.getReplyList(bno);




        return list;
    }

    @GetMapping("/reply/myreply")
    @ResponseBody
    public String myreply(Model model){

        System.out.println("GET /reply/myreply");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        UserDto dto = principalDetails.getUser();

        if(replyRepository.existsByNickname(dto.getNickname())){
            return "true";
        } else{
            return "false";
        }
    }
    //-------------------
    //댓글 카운트
    //-------------------
    @GetMapping("/reply/count")
    public Long getCount(Long bno){
        log.info("GET /reply/count " + bno);
        Long cnt = boardService.getReplyCount(bno);

        return cnt;
    }
}