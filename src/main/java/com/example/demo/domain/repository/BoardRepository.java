package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {

    List<Board> findAll();

    boolean existsByNumber(Long number);

    @Query("SELECT b FROM Board b WHERE email = :email ORDER BY date DESC")
    List<Board> getBoardByEmailOrderByDateDesc(@Param("email") String email);

    @Query("SELECT b FROM Board b WHERE number = :number")
    List<Object> findByNumber(@Param("number") Long num);

    @Query("SELECT b FROM Board b WHERE number = :number")
    Optional<Board> findByNum(@Param("number") Long number);

    @Query("SELECT b FROM Board b WHERE contents LIKE %:keyword%")
    List<Board> findByContents(@Param("keyword") String keyword);



}