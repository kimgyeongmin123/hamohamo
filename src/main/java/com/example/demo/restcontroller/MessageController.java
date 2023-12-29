package com.example.demo.restcontroller;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.domain.dto.MessageDto;
import com.example.demo.domain.entity.Message;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.FollowRepository;
import com.example.demo.domain.repository.NotificationRepository;
import com.example.demo.domain.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    //팔로우 리스트 불러오기
    @GetMapping("/message/rooms")
    public String getRooms(Model model, Authentication authentication){
        //현재 유저 정보 가져오기
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        System.out.println("유저 닉네임 : "+principal.getNickname());

        String email = principal.getUsername();

        //팔로우한 사람의 정보 가져오기
        List<User> followingList = followRepository.findByFollowNickname(email);

        System.out.println("followingList : "+followingList);

        model.addAttribute("followingList",followingList);

        //알림갯수
        int notiCount = notificationRepository.countByMyNickname(principal.getUser().getNickname());

        model.addAttribute("notiCount", notiCount);

        return "message/rooms";
    }

    @GetMapping("message/chat/{sendNickname}")
    public String messageList(@PathVariable(name = "sendNickname") String sendNickname,
                              Authentication authentication,Model model){
        //현재 유저 정보 가져오기
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        //현재 유저의 이메일 저장
        String nickname = principal.getNickname();

        //메시지 리스트 가져오기
        List<Message> list = messageService.getMessageList(nickname,sendNickname);

        if(list==null){
            list = new ArrayList<>();
        }

        model.addAttribute("list",list);

        return "message/chat";


    }

    @PostMapping("/message/send")
    @ResponseBody
    public ResponseEntity<String> sendMessage(
            @RequestParam("otherNick") String otherNick,
            @RequestParam("content") String content,
            Authentication authentication
    ) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        String senderNick = principal.getNickname();

        // 메시지 전송 로직 구현 (메시지를 DB에 저장하도록 messageService 활용)
        Message message = Message.builder()
                .send_nick(senderNick)
                .recv_nick(otherNick)
                .content(content)
                .build();

        messageService.sendMessage(message);

        // 전송 성공 시 응답
        return ResponseEntity.ok("Message sent successfully");
    }




}