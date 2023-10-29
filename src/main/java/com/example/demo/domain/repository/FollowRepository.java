package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Follow;
import com.example.demo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByFollowerAndFollowing(User follower, User following);

//    @Query("SELECT f.following.email FROM Follow f WHERE f.follower.email = :followerEmail")

    @Query("SELECT u FROM Follow f INNER JOIN User u ON f.following.email = u.email WHERE f.follower.email = :followerEmail")
    List<User> findByFollowNickname(@Param("followerEmail") String followerEmail);

    @Query("SELECT COALESCE(COUNT(f.follower.email), 0) as cnt FROM Follow f WHERE f.follower.email=:followerEmail GROUP BY f.follower.email")
    String CntFollowing(@Param("followerEmail") String followerEmail);
}