package com.example.demo.controller;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.domain.dto.BoardDto;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.Reply;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.*;
import com.example.demo.domain.service.BoardService;
import com.example.demo.domain.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.Notification;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
//
@Controller
@Slf4j
public class BoardController {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private NotificationRepository notificationRepository;


    @GetMapping("/list")
    public void list(Model model, Authentication authentication){
        log.info("GET /list");

        // 현재유저정보 가져오기
        PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();

        String currentUser = principal.getUser().getEmail();

        // 팔로우 리스트를 가져오기
        List<User> followList = followService.getFollowList(currentUser);
//        List<String> followProfileList = followService.getFollowListProfile(currentUser);

        // 게시물을 날짜 기준으로 내림차순 정렬
        List<Object[]> list = boardService.getBoardList();

        List<Map<String, Object>> dataList = new ArrayList<>();

        for (Object[] row : list) {
            Map<String, Object> data = new HashMap<>();
            data.put("board", row[0]); // 여기에서 row[0]는 Board 객체
            data.put("profile", row[1]); // 여기에서 row[1]은 profile 문자열
            data.put("cnt", row[2]); //cnt 가져옵니다.
            dataList.add(data);
        }

        System.out.println("dataList : " + dataList);

        //알림갯수
        int notiCount = notificationRepository.countByMyNickname(principal.getUser().getNickname());

        model.addAttribute("dataList", dataList);
        model.addAttribute("followList", followList);
        model.addAttribute("notiCount", notiCount);

        System.out.println("notiCount : " + notiCount);

    }

    @PostMapping("/post")
    public String post_post(
            BoardDto dto,Authentication authentication
    ) throws IOException {
        log.info("POST /post"+dto+"그리고 "+authentication);

        dto.setDate(LocalDateTime.now());
        PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();
        dto.setEmail(principal.getUsername());
        dto.setNickname(principal.getNickname());

        boardService.addBoard(dto);

        return "redirect:/list";
    }

    @GetMapping("/update")
    public void update(@RequestParam Long number, Model model){
        log.info("GET /update no : " + number);

        //-----------------------------------------------------------------------------------
        // 현재 인증된 사용자의 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        //--------------------------------------------------------------------------------------

        //서비스 실행
        Board board = boardService.getBoardOne(number);
        List<String> files = board.getFiles();

        PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();
        //알림갯수
        int notiCount = notificationRepository.countByMyNickname(principal.getUser().getNickname());
        model.addAttribute("notiCount", notiCount);

        model.addAttribute("files", files);
        model.addAttribute("board", board);


    }

    @PostMapping("/update")
    public String postUpdate(@Valid BoardDto dto,
                             @Param("newContents") String newContents,
                             BindingResult bindingResult,
                             Model model) throws IOException {
        log.info("POST /update number: " + dto.getNumber());

        if (bindingResult.hasFieldErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info(error.getField() + " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "mypage"; // 폼 다시 표시
        }

        boolean isAdd = boardService.updateBoard(dto,newContents);


        if (isAdd) {
            return "redirect:/list";
        }
        return "redirect:/mypage";

    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("number") Long number, RedirectAttributes redirectAttributes) {
        log.info("DELETE /delete number " + number);

        boolean isRemoved = boardService.deleteBoard(number);
        if (isRemoved) {
            // 성공적으로 삭제된 경우 리다이렉트와 메시지 전달
            redirectAttributes.addFlashAttribute("message", "게시물이 삭제되었습니다.");
            return ResponseEntity.ok("Deleted successfully.");
        } else {
            // 삭제에 실패한 경우 리다이렉트와 메시지 전달
            redirectAttributes.addFlashAttribute("error", "게시물 삭제에 실패했습니다.");
            return ResponseEntity.ok("Deletion failed.");
        }
    }

    @GetMapping("/read/{number}")
    public String read(@PathVariable("number") Long number, Model model, HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        System.out.println("GET /read/"+number);
        Board board = boardService.getBoardOne(number);
        String profile = boardService.getProfileForBoard(number);

        System.out.println(board);

        List<String> files = board.getFiles();
        System.out.println(files);

        PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();
        //알림갯수
        int notiCount = notificationRepository.countByMyNickname(principal.getUser().getNickname());
        model.addAttribute("notiCount", notiCount);
        model.addAttribute("board", board);
        model.addAttribute("files", files);
        model.addAttribute("profile", profile);

        //-----------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------

        //클라이언트에서 전송한 모든 쿠키 cookies에 저장
        Cookie[] cookies = request.getCookies();
        log.info("cookies"+cookies);

        // "조회한 게시물"을 나타내는 쿠키의 이름
        String cookieName = "read_" + number;

        // 쿠키 확인하여 중복 조회 방지
        boolean isAlreadyRead = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    // 이미 해당 게시물을 조회한 경우
                    isAlreadyRead = true;
                    break;
                }
            }
        }

        if (!isAlreadyRead) {
            // 조회수를 증가시키는 코드 추가
            boardService.hits_count(number);

            // 쿠키 생성 및 클라이언트에게 전송
            Cookie readCookie = new Cookie(cookieName, "true");
            readCookie.setPath("/"); // 쿠키의 범위 설정
            response.addCookie(readCookie);
        }
        //--------------------------------------------------------------------------------------

        return "read";
    }

    @GetMapping("/like/{number}")
    @ResponseBody
    public boolean like(@PathVariable("number") Long number) {
        System.out.println("[보드컨트롤러]의 라이크(겟)입니다.");
        // 현재 인증된 사용자의 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // 게시물 번호로 해당 게시물 정보 가져오기
        Optional<Board> boardOptional = boardRepository.findByNum(number);

        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            User user = userRepository.findById(email).get();

            System.out.println("[보드컨트롤러]보드서비스의 에드라이크 호출할거니?");
            boolean isLiked = boardService.addLike(user, board);
            System.out.println("[보드컨트롤러]보드서비스의 에드라이크 호출끝");

            //html로 isLiked 보내기 왜?
//            model.addAttribute("isLiked", isLiked);

            System.out.println("isLiked : " + isLiked);

            return isLiked;

//            if (isLiked) {
//                return ResponseEntity.ok("Liked successfully.");
//            } else {
//                return ResponseEntity.ok("Already liked.");
//            }
        } else {
            return false;
        }
    }

    @GetMapping("/get-like-status/{number}")
    @ResponseBody
    public String likeStatus(@PathVariable("number") Long number){

        // 현재 인증된 사용자의 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // 게시물 번호로 해당 게시물 정보 가져오기
        Optional<Board> boardOptional = boardRepository.findByNum(number);

        Board board = boardOptional.get();
        User user = userRepository.findById(email).get();

        if (boardService.isLiked(user, board)) {
            return "true";
        } else {
            return "false";
        }

    }

    @GetMapping("/list/search-contents")
    public String search(String keyword, Model model, Authentication authentication){

        List<Object[]> searchList = boardService.search_contents(keyword);
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 현재유저정보 가져오기
        PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();

        String currentUser = principal.getUser().getEmail();

        // 팔로우 리스트를 가져오기
        List<User> followList = followService.getFollowList(currentUser);

        for (Object[] row : searchList) {
            Map<String, Object> data = new HashMap<>();
            data.put("board", row[0]); // 여기에서 row[0]는 Board 객체
            data.put("profile", row[1]); // 여기에서 row[1]은 profile 문자열
            data.put("cnt", row[2]); //cnt 가져옵니다.

            System.out.println(data);

            dataList.add(data);
        }

        System.out.println("dataList : "+ dataList);

        //알림갯수
        int notiCount = notificationRepository.countByMyNickname(principal.getUser().getNickname());
        model.addAttribute("notiCount", notiCount);

        model.addAttribute("boardList",dataList);
        model.addAttribute("followList", followList);

        return "search-contents";
    }

    //--------------------------------
    // /Board/reply/delete
    //--------------------------------
    @GetMapping("/reply/delete/{bno}/{rnumber}")
    public String delete(@PathVariable Long bno, @PathVariable Long rnumber){
        log.info("GET reply/delete bno,rnumber " + rnumber + " " + rnumber);

        // 현재 인증된 사용자의 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // UserRepository를 사용하여 사용자 정보 가져오기
        User user = userRepository.findByEmail(email);

        System.out.println("로그인한 유저의 닉네임 : " + user.getNickname());
        System.out.println("이 rnumber를 작성한 사용자의 닉네임 : "+replyRepository.FindNicknameByRnumber(rnumber));

        if(Objects.equals(replyRepository.FindNicknameByRnumber(rnumber), user.getNickname())){
            boardService.deleteReply(rnumber);
        }


        return "redirect:/read/"+bno;
    }


    //--------------------------------
    // /board/reply/thumbsup
    //--------------------------------
    @GetMapping("/reply/thumbsup")
    public String thumbsup(Long bno, Long rnumber)
    {

        boardService.thumbsUp(rnumber);
        return "redirect:/read/"+bno;
    }
    //--------------------------------
    // /board/reply/thumbsdown
    //--------------------------------
    @GetMapping("/reply/thumbsdown")
    public String thumbsudown(Long bno, Long rnumber)
    {
        boardService.thumbsDown(rnumber);
        return "redirect:/read/"+bno;
    }

    //보드number로 내페이지 구분
    @GetMapping(value ="/whopage/{number}")
    public String whopage(@PathVariable("number") Long number){

        String boardEmail = boardService.whopageS(number);

        // 현재 인증된 사용자의 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if(email.equals(boardEmail)){
            return "redirect:/mypage";
        }else if(!email.equals(boardEmail)){
            return "redirect:/nampage?boardEmail=" + boardEmail;
        }

        return null;
    }

    @GetMapping("/nampage")
    public String nampage(@RequestParam(value = "boardEmail") String boardEmail, Model model, Authentication authentication) {
        if (boardEmail != null) {
            System.out.println("남페이지 겟매핑 남의이메일 : " + boardEmail);

            //유저정보 조회
            User user = userRepository.findByEmail(boardEmail);

            //보드정보 조회
            List<Board> namBoards = boardRepository.getBoardByEmailOrderByDateDesc(boardEmail);

            System.out.println("남에 보드정보 : "+ namBoards);

            // 팔로우 리스트를 가져오기
            List<User> followList = followService.getFollowList(boardEmail);

            //팔로워 리스트를 가져오기
            List<User> followerList = followService.getFollowerList(boardEmail);

            String cntFollowing = followRepository.CntFollowing(boardEmail);
            if (cntFollowing==null){
                cntFollowing="0";
            }
            String cntFollower = followRepository.CntFollower(boardEmail);
            if (cntFollower==null){
                cntFollower="0";
            }

            model.addAttribute("cntFollowing",cntFollowing);
            model.addAttribute("cntFollower",cntFollower);

            //남의 게시물 정보 보내기
            model.addAttribute("namBoards", namBoards);

            //남의 유저 정보 보내기
            model.addAttribute("namUser", user);

            //팔로우/팔로워 목록 보내기
            model.addAttribute("followList", followList);
            model.addAttribute("followerList", followerList);

            PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();
            //알림갯수
            int notiCount = notificationRepository.countByMyNickname(principal.getUser().getNickname());
            model.addAttribute("notiCount", notiCount);
        }
        return "nampage";
    }

    @GetMapping("/nampage/{email}")
    public String nampage2(@PathVariable (value = "email") String email, Model model, Authentication authentication) {
        if (email != null) {
            System.out.println("남페이지 겟매핑 남의이메일 : " + email);

            //유저정보 조회
            User user = userRepository.findByEmail(email);

            //보드정보 조회
            List<Board> namBoards = boardRepository.getBoardByEmailOrderByDateDesc(email);

            System.out.println("남에 보드정보 : "+ namBoards);

            // 팔로우 리스트를 가져오기
            List<User> followList = followService.getFollowList(email);

            //팔로워 리스트를 가져오기
            List<User> followerList = followService.getFollowerList(email);

            String cntFollowing = followRepository.CntFollowing(email);
            if (cntFollowing==null){
                cntFollowing="0";
            }
            String cntFollower = followRepository.CntFollower(email);
            if (cntFollower==null){
                cntFollower="0";
            }

            model.addAttribute("cntFollowing",cntFollowing);
            model.addAttribute("cntFollower",cntFollower);

            //남의 게시물 정보 보내기
            model.addAttribute("namBoards", namBoards);

            //남의 유저 정보 보내기
            model.addAttribute("namUser", user);

            //팔로우/팔로워 목록 보내기
            model.addAttribute("followList", followList);
            model.addAttribute("followerList", followerList);

            PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();
            //알림갯수
            int notiCount = notificationRepository.countByMyNickname(principal.getUser().getNickname());
            model.addAttribute("notiCount", notiCount);
        }
        return "nampage";
    }

    @GetMapping("/draw")
    public void draw(Model model, Authentication authentication){
        log.info("GET /draw");

//      // 현재유저정보 가져오기
        PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();

        //알림갯수
        int notiCount = notificationRepository.countByMyNickname(principal.getUser().getNickname());
        model.addAttribute("notiCount", notiCount);

    }

}