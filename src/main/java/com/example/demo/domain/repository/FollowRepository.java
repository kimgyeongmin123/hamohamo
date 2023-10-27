package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Follow;
import com.example.demo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByFollowerAndFollowing(User follower, User following);

    @Query("SELECT f.following.email FROM Follow f WHERE f.follower.email = :followerEmail")
    List<String> findByFollow(@Param("followerEmail") String followerEmail);
}