package com.example.demo.restcontroller;


import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.domain.entity.BoardNotification;
import com.example.demo.domain.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boardnotification")
@Slf4j
public class BoardNotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping(value = "/read",produces = MediaType.APPLICATION_JSON_VALUE)
    public  List<BoardNotification> f1(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        String writenickname = principalDetails.getNickname();
        System.out.println("writenickname : " + writenickname);
        List<BoardNotification> list =  notificationRepository.findAllByNickname(writenickname);

        //읽음 처리
        for(BoardNotification boardNotification : list) {
            System.out.println(boardNotification);
            boardNotification.setIsread(true);  //읽음처리
            notificationRepository.save(boardNotification);//저장
        }
        return list;

    }
    @GetMapping(value = "/isexist",produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isexist(Authentication authentication)
    {
        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        String writenickname = principalDetails.getNickname();
        System.out.println("writenickname : " + writenickname);
        List<BoardNotification> list =  notificationRepository.findAllByNickname(writenickname);

        return list.size()>0;
    }


}