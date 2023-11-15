package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {


    @Query("SELECT r FROM Reply r WHERE bno = :bno ORDER BY rnumber DESC")
    List<Reply> GetReplyByBnoDesc(@Param("bno") Long bno);

    @Query("SELECT COUNT(r) FROM Reply r WHERE bno = :bno")
    Long GetReplyCountByBnoDesc(@Param("bno") Long bno);

    @Query("SELECT nickname FROM Reply WHERE rnumber = :rnumber")
    String FindNicknameByRnumber(@Param("rnumber") Long rnumber);




}