package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.Heart;
import com.example.demo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    boolean existsByUserAndBoard(User user, Board board);

    void deleteByUserAndBoard(User user, Board board);
}
