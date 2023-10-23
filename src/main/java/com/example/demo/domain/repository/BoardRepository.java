package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {



    //List<Board> findAll();

    boolean existsByNumber(Long number);

    @Query("SELECT b FROM Board b WHERE email = :email ORDER BY date DESC")
    List<Board> getBoardByEmailOrderByDateDesc(@Param("email") String email);

    @Query("SELECT b FROM Board b WHERE number = :number")
    Optional<Board> findByNum(@Param("number") Long number);

    @Query("SELECT b FROM Board b WHERE contents LIKE %:keyword%")
    List<Board> findByContents(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query("DELETE FROM Board b WHERE b.nickname = :nickname")
    void deleteByNickname(@Param("nickname") String nickname);

    //조인하여 프로필조회 (보드 number 에 따라 프로필만 select)
    @Query("SELECT b,u.profile FROM Board b INNER JOIN User u ON b.email = u.email")
    List<Object[]> findJoin();



}