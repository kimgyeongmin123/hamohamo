package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.nick = :nickname AND m.other_nick = :sendNickname) ORDER BY m.sendTime ASC")
    List<Message> getMessageBySendEmail(@Param("nickname") String nickname, @Param("sendNickname") String sendNickname);

}