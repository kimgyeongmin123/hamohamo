package com.example.demo.controller;

import com.example.demo.domain.dto.BoardDto;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.BoardRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.domain.service.BoardService;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class BoardController {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/list")
    public String list(Model model){
        log.info("GET /list");

        // 게시물을 날짜 기준으로 내림차순 정렬하여 가져옵니다.
        List<Board> list = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));

        System.out.println("Board's list : " + list);


        //dto -> entity
        List<BoardDto> boardDtos = list.stream()
                .map(BoardDto::Of)
                .collect(Collectors.toList());


        System.out.println("Board's boardDtos : " + boardDtos);

        model.addAttribute("board", list);


        return "list";
    }

    @PostMapping("/post")
    public String post_post(
            @Valid BoardDto dto,
            @RequestParam("files") MultipartFile[] files,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        log.info("POST /post");

        if (bindingResult.hasFieldErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info(error.getField() + " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "/list"; // 폼 다시 표시
        }

        if (files == null || files.length == 0 || files[0].isEmpty()) {
            // 이미지를 첨부하지 않은 경우
            redirectAttributes.addFlashAttribute("error", "이미지를 첨부해주세요.");
            return "redirect:/list";
        }

        boolean isAdd = boardService.addBoard(dto);

        if (isAdd) {
            return "redirect:/list";
        }
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


        System.out.println("update's dto : " + board);

        // 모델에 게시물 정보 전달
        model.addAttribute("boardDto", board);


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
    public String read(@PathVariable("number") Long number, Model model, HttpServletRequest request, HttpServletResponse response){
        log.info("GET /read/"+number);

        //-----------------------------------------------------------------------------------
        // 현재 인증된 사용자의 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // UserRepository를 사용하여 사용자 정보 가져오기
        User user = userRepository.findById(email).get();


        //--------------------------------------------------------------------------------------

        //서비스 실행
        Board board = boardService.getBoardOne(number);


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

        model.addAttribute("boardDto",board);

        return "read";
    }

    @GetMapping("/like/{number}")
    public ResponseEntity<String> like(@PathVariable("number") Long number, Model model) {
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
            model.addAttribute("isLiked", isLiked);

            System.out.println("isLiked : " + isLiked);

            if (isLiked) {
                return ResponseEntity.ok("Liked successfully.");
            } else {
                return ResponseEntity.ok("Already liked.");
            }
        } else {
            return ResponseEntity.ok("Board not found.");
        }
    }

    @GetMapping("/list/search-contents")
    public String search(String keyword, Model model){
        List<Board> searchList = boardService.search_contents(keyword);
        model.addAttribute("boardList",searchList);
//-----------------------------------------------------------------------------------
        // 현재 인증된 사용자의 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // UserRepository를 사용하여 사용자 정보 가져오기
        User user = userRepository.findById(email).get();

//--------------------------------------------------------------------------------------
        return "search-contents";
    }

    //--------------------------------
    // /Board/reply/delete
    //--------------------------------
    @GetMapping("/reply/delete/{bno}/{rnumber}")
    public String delete(@PathVariable Long bno, @PathVariable Long rnumber){
        log.info("GET reply/delete bno,rnumber " + rnumber + " " + rnumber);

        boardService.deleteReply(rnumber);

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


}