package com.example.demo.listener;

import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.BoardNotification;
import com.example.demo.domain.entity.Reply;
import com.example.demo.domain.repository.BoardRepository;
import com.example.demo.domain.repository.NotificationRepository;
import com.example.demo.websocket.BoardNotificationSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;

@Component
public class ReplyAddEventListener implements ApplicationListener<ReplyEvent> {

    @Autowired
    private BoardNotificationSocketHandler webSockerHandler;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void onApplicationEvent(ReplyEvent event) {
        System.out.println("[EVENT] Reply add :" + event);
        System.out.println("ws : " + webSockerHandler);

        Reply reply = event.getReply();
        Long bno = reply.getBoard().getNumber();

        System.out.println("reply~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ : "+ reply);
        System.out.println("BNO~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ : " + bno);

        Board board = boardRepository.findByNum(bno).get();
        System.out.println("board : " + board);

        //접속해있다면 : 소켓 연결해서 해당 소켓을 찾아서 전달할것 + DB저장
        WebSocketSession webSocketSession = webSockerHandler.findWebSocketSession(board.getEmail());

        if(webSocketSession!=null){
            TextMessage message = new TextMessage("메시지가 도착했습니다.");
            try {
                webSockerHandler.handleMessage(webSocketSession,message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //DB저장
        BoardNotification notification = new BoardNotification();
        notification.setBid(bno);
        notification.setRdate(LocalDateTime.now());
        notification.setWritenickname(board.getNickname());
        notification.setReplynickname(reply.getNickname());
        notification.setMessage(reply.getContent());
        notification.setIsread(false);

        notificationRepository.save(notification);







    }
}