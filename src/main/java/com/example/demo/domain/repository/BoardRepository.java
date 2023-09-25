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


    @Query("SELECT b from Board b WHERE email = :email")
    List<Board> getBoardByEmail(@Param("email") String email);
}