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

    //내가 팔로우 누른 사람의 수
    @Query("SELECT COUNT(f.follower.email) as cnt FROM Follow f WHERE f.follower.email=:followerEmail GROUP BY f.follower.email")
    String CntFollowing(@Param("followerEmail") String followerEmail);

    //나를 팔로우 누른 사람의 수
    @Query("SELECT COUNT(f.following.email) as cnt FROM Follow f WHERE f.following.email=:followerEmail GROUP BY f.follower.email")
    String CntFollower(@Param("followerEmail") String followerEmail);
}