package com.example.demo.domain.service;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.BoardRepository;
import com.example.demo.domain.repository.HeartRepository;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    HeartRepository heartRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    public boolean joinMember(UserDto dto, Model model, HttpServletRequest request){

        //Email 중복체크
//        if (isEmailAlreadyTaken(dto.getEmail())) {
//            model.addAttribute("email", "이미 사용중인 이메일입니다.");
//            return false;
//        }

        //패스워드 일치하는지 확인
//        if(!dto.getPassword().equals(dto.getRepassword())){
//            model.addAttribute("repassword","패스워드가 일치하지 않습니다.");
//            //일치안하면 FALSE(가입실패)
//            return false;
//        }

        dto.setRole("ROLE_USER");
        dto.setProfile("/images/basic_profile.png");
        dto.setPassword(passwordEncoder.encode(dto.getPassword()) );

        User user = UserDto.dtoToEntity(dto);
        System.out.println("joinMember's user : " + user);

        userRepository.save(user);

        return true;
    }

    @Transactional(rollbackFor = SQLException.class)
    public Boolean UserUpdate(String email, String newNickname, String newBirth, String newPhone){
        try {
            User user = userRepository.findByEmail(email);

            if (user != null) {
                //원래 닉네임 따로 저장
                String oldNickname = user.getNickname();

                System.out.println("oldNickname: " + oldNickname);

                //바뀐 정보들을 set
                user.setNickname(newNickname);
                user.setBirth(newBirth);
                user.setPhone(newPhone);
//                user.setZipcode(newZipcode);
//                user.setAddr1(newAddr1);
//                user.setAddr2(newAddr2);

                //이거는 테이블의 닉네임도 바꾸는 것이에요 :) 이거는 때려죽여도 잘돼요
                entityManager.createNativeQuery("UPDATE board SET nickname = :newNickname WHERE nickname = :oldNickname")
                        .setParameter("newNickname", newNickname)
                        .setParameter("oldNickname", oldNickname)
                        .executeUpdate();

                // 바뀐 정보들을 save
                userRepository.save(user);

                System.out.println("user: " +user);


                //성공의 의미인 true를 반환
                return true;
            } else {
                return false; // 사용자를 찾지 못한 경우
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 업데이트 중 예외 발생
        }

    }

    @Transactional
    public boolean withdrawUser(String email, String password) {
        try {
            Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
            if (userOptional.isPresent()) {
                User user = userOptional.get(); // Optional에서 User를 얻는 방법
                // 비밀번호 확인
                if (passwordEncoder.matches(password, user.getPassword())) {

                    heartRepository.deleteByUser(user.getEmail());
                    boardRepository.deleteByNickname(user.getNickname());
                    userRepository.delete(user);
                    return true;
                }
            }
            return false; // 사용자를 찾지 못하거나 비밀번호가 일치하지 않음
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 예외 발생 시 탈퇴 실패
        }
    }

    public boolean isEmailAlreadyTaken (String email){
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(email));
        return existingUser.isPresent();
    }

    public List<User> search_nickname(String keyword) {
        List<User> userList = userRepository.findByNickname(keyword);
        System.out.println("userList: " + userList);
        return userList;
    }

    public void updateProfile(User user) {

        userRepository.save(user);
    }



}