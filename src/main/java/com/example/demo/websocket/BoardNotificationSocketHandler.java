package com.example.demo.websocket;

import com.example.demo.domain.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
public class BoardNotificationSocketHandler extends TextWebSocketHandler {

    @Autowired
    private BoardRepository boardRepository;
    // Authentication 객체를 주입받을 필드 추가

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();




    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("연결 성공!...session : " + session +" | UserEmail  : " + session.getPrincipal().getName() );
        String username = session.getPrincipal().getName();

        CLIENTS.put(username.trim(), session);
        System.out.println(username+" 소켓 등록 전체소켓 : "+ CLIENTS.size());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        CLIENTS.remove( session.getPrincipal().getName());

    }

    public WebSocketSession findWebSocketSession(String username){

        for(String key : CLIENTS.keySet()){
            if(key.equals(username)){
                return CLIENTS.get(key);
            }
        }
        return null;
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Server Recv Message : " + message);
        String id = session.getPrincipal().getName();  //메시지를 보낼 아이디
        CLIENTS.entrySet().forEach( arg->{

            //if(!arg.getKey().equals(id)) {  //같은 아이디가 아니면 메시지를 전달합니다.
//            if(true) {  //접속한 모든 세션에 전달
//                try {
//
//                    arg.getValue().sendMessage(message);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            if(arg.getKey().equals(id)) //해당 아이디이면 전달
            {
                try {
                    arg.getValue().sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });
    }



}