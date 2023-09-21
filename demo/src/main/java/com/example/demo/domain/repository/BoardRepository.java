package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {

    List<Board> findAll();

    boolean existsByNumber(Long number);
}
