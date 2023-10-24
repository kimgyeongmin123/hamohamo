package com.example.demo.domain.repository;

import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.Heart;
import com.example.demo.domain.entity.User;
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


    //모든 게시물의 정보 조회
    //List<Board> findAll();

    boolean existsByNumber(Long number);

    //이메일로 보드정보 조회
    @Query("SELECT b FROM Board b WHERE email = :email ORDER BY date DESC")
    List<Board> getBoardByEmailOrderByDateDesc(@Param("email") String email);

    @Query("SELECT b FROM Board b WHERE number = :number")
    Optional<Board> findByNum(@Param("number") Long number);

    //read에서 사용할 프로필쿼리문
    @Query("SELECT u.profile FROM Board b INNER JOIN User u On b.email = u.email WHERE number = :number")
    String findByNumProfile(@Param("number") Long number);

    //모든 글의 프로필만 조회
//    @Query("SELECT u.profile FROM Board b INNER JOIN User u On b.email = u.email")
//    String findProfile();

    @Query("SELECT b,u.profile FROM Board b INNER JOIN User u On b.email = u.email WHERE contents LIKE %:keyword% ORDER BY b.date DESC")
    List<Object[]> findByContents(@Param("keyword") String keyword);

    //보드 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Board b WHERE b.nickname = :nickname")
    void deleteByNickname(@Param("nickname") String nickname);

    //조인하여 프로필과 Board 함께 조회(list에서 사용)
    @Query("SELECT b,u.profile FROM Board b INNER JOIN User u ON b.email = u.email ORDER BY b.date DESC")
    List<Object[]> findJoin();

    //해당보드를 작성한 유저의 이메일 조회
    @Query("SELECT u.email FROM User u INNER JOIN Board b ON u.email = b.email WHERE b.number=:number")
    String whoboard(@Param("number") Long number);

}