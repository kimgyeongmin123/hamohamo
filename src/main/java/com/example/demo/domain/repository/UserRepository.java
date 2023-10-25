package com.example.demo.domain.repository;

import com.example.demo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {


    //이메일로 유저정보조회
    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByPhone(String value);

    User findBynameAndPhoneAndQuestionAndAnswer(String name, String phone, String question, String answer);

    @Query("SELECT u FROM User u WHERE nickname LIKE %:keyword% AND nickname NOT LIKE :mynickname")
    List<User> findByNickname(@Param("keyword") String keyword, @Param("mynickname") String mynickname);


}