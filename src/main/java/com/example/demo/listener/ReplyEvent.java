package com.example.demo.listener;

import com.example.demo.domain.entity.Reply;
import org.springframework.context.ApplicationEvent;


public class ReplyEvent extends ApplicationEvent {

    private Reply reply;
    public ReplyEvent(Object source, Reply reply) {
        super(source);
        this.reply = reply;
    }
    public Reply getReply() {
        return reply;
    }
    @Override
    public String toString() {
        return "ReplyEvent{" +
                "replyEntity=" + reply +
                '}';
    }

}