package com.example.demo.domain.service;

import com.example.demo.controller.BoardController;
import com.example.demo.domain.dto.BoardDto;
import com.example.demo.domain.dto.ReplyDto;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.Heart;
import com.example.demo.domain.entity.Reply;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.BoardRepository;
import com.example.demo.domain.repository.HeartRepository;
import com.example.demo.domain.repository.ReplyRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {


    //경민-학원컴퓨터
    //private String uploadDir = "C:/Users/Administrator/Desktop/망고(휴지통에 넣지말아주세요)/hamo/hamohamo/src/main/resources/static/images";
    //경민-집컴퓨터
    private String uploadDir = "C:/Users/82108/OneDrive/바탕 화면/hamohamo/hamohamo/src/main/resources/static/images";

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private HeartRepository heartRepository;

    @Transactional(rollbackFor = SQLException.class)
    public boolean addBoard(BoardDto dto) throws IOException {
        System.out.println("upload File Count : " +dto.getFiles().length);

        Board board = new Board();
        board.setNickname(dto.getNickname());
        board.setContents(dto.getContents());
        board.setDate(LocalDateTime.now());
        board.setHits(0L);
        board.setLike_count(0L);

        MultipartFile [] files = dto.getFiles();

        System.out.println("board : " + board);

        List<String> filenames = new ArrayList<String>();
        List<String> filesizes = new ArrayList<String>();

        if(dto.getFiles().length >= 1 && dto.getFiles()[0].getSize()!=0L)
        {
            //Upload Dir 미존재시 생성
            String path = uploadDir+ File.separator+dto.getNickname();
            File dir = new File(path);

            // 이메일과 UUID 추출
            String extractedEmail = dto.getNickname();

            // 이메일과 UUID를 이용하여 디렉토리 경로 생성
            String dirPath = "http://localhost:8080/images/" + extractedEmail;

            if(!dir.exists()) {
                dir.mkdirs();
            }
            //board에 경로 추가
            board.setDirpath(dirPath);

            for(MultipartFile file  : dto.getFiles())
            {
                System.out.println("--------------------");
                System.out.println("FILE NAME : " + file.getOriginalFilename());
                System.out.println("FILE SIZE : " + file.getSize() + " Byte");
                System.out.println("--------------------");

                //파일명 추출
                String filename = file.getOriginalFilename();

                //파일객체 생성
                File fileobj = new File(path,filename);

                //업로드
                file.transferTo(fileobj);

                //filenames 저장
                filenames.add(filename);
                filesizes.add(file.getSize()+"");

//                //섬네일이미지 파일 만들기
//
//                File thumbnailFile = new File(path, "s_" + filename);
//
//                BufferedImage bo_img = ImageIO.read(fileobj);
//                double ratio = 3;
//                int width = (int) (bo_img.getWidth() / ratio);
//                int height = (int) (bo_img.getHeight() / ratio);
//
//                Thumbnails.of(fileobj)
//                        .size(width, height)
//                        .toFile(thumbnailFile);
            }
        }

        board.setFilename(filenames.toString());
        board.setFilesize(filesizes.toString());

        board = boardRepository.save(board);
        boolean issaved = boardRepository.existsByNumber(board.getNumber());
        System.out.println("issaved : " + issaved);
        return issaved;
    }

    @Transactional(rollbackFor = SQLException.class)
    public boolean deleteBoard(Long number){
        System.out.println("deleteBoard할거임!!!!!!!!! : " + number);

        Optional<Board> boardOptional = boardRepository.findByNum(number);


        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            if(board.getDirpath()!=null)
                // 게시물의 이미지 파일들을 삭제
                deleteImageFiles(board.getDirpath());
            // 게시물 삭제
            heartRepository.deleteByBoard(board);
            boardRepository.delete(board);

            return true;
        }
        return false;
    }

    private void deleteImageFiles(String dirPath){
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete(); // 이미지 파일 삭제
                }
            }
            dir.delete(); // 디렉토리 삭제
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public boolean updateBoard(Long number, String newContents) {

        System.out.println("updateBoard!!!!!"+number);
        // 게시물 번호로 해당 게시물 정보 가져오기
        Optional<Board> boardOptional = boardRepository.findByNum(number);

        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();

            // 내용만 업데이트
            board.setContents(newContents);

            // 수정된 게시물 저장
            boardRepository.save(board);

            return true; // 수정 성공
        } else {
            return false; // 게시물이 존재하지 않는 경우
        }
    }

    @Transactional(rollbackFor = SQLException.class)
    public void hits_count(Long number){
        System.out.println("조회수 카운트 할거임!?!??!?!");
        Board board = boardRepository.findByNum(number).get();
        board.setHits(board.getHits()+1);
        boardRepository.save(board);
    }



    public void addReply(Long bno,String content, String nickname){

        Reply reply = new Reply();
        Board board = new Board();
        board.setNumber(bno);

        reply.setBoard(board);
        reply.setContent(content);
        reply.setNickname(nickname);
        reply.setDate(LocalDateTime.now());
        reply.setLikecount(0L);
        reply.setUnlikecount(0L);

        replyRepository.save(reply);
    }
    public List<ReplyDto> getReplyList(Long bno) {
        List<Reply> replyList =  replyRepository.GetReplyByBnoDesc(bno);

        List<ReplyDto> returnReply  = new ArrayList();


        if(!replyList.isEmpty()) {
            for(int i=0;i<replyList.size();i++) {

                ReplyDto dto = new ReplyDto();
                dto.setBno(replyList.get(i).getBoard().getNumber());
                dto.setRnumber(replyList.get(i).getRnumber());
                dto.setNickname(replyList.get(i).getNickname());
                dto.setContent(replyList.get(i).getContent());
                dto.setLikecount(replyList.get(i).getLikecount());
                dto.setUnlikecount(replyList.get(i).getUnlikecount());
                dto.setDate(replyList.get(i).getDate());

                returnReply.add(dto);

            }
            return returnReply;
        }
        return null;
    }

    public Long getReplyCount(Long bno) {
        return replyRepository.GetReplyCountByBnoDesc(bno);

    }


    public void deleteReply(Long rnunmber) {
        replyRepository.deleteById(rnunmber);
    }

    public void thumbsUp(Long rnunmber) {
        Reply reply =  replyRepository.findById(rnunmber).get();
        reply.setLikecount(reply.getLikecount()+1L);
        replyRepository.save(reply);
    }

    public void thumbsDown(Long rnunmber) {
        Reply reply =  replyRepository.findById(rnunmber).get();
        reply.setUnlikecount(reply.getUnlikecount()+1L);
        replyRepository.save(reply);
    }

    @Transactional(rollbackFor = SQLException.class)
    public List<Board> search_contents(String keyword){
        List<Board> boardList = boardRepository.findByContents(keyword);
        return boardList;
    }

    public boolean addLike(User user, Board board) {
        System.out.println("[보드서비스]에드라이크 호출완료했슴다");
        // 중복 좋아요를 방지하기 위해 이미 좋아요를 눌렀는지 확인
        if (!heartRepository.existsByUserAndBoard(user, board)) {
            System.out.println("[보드서비스]의 에드라이크 : 좋아요를 안눌러서 등록처리합니다.");
            Heart heart = new Heart();
            heart.setUser(user);
            heart.setBoard(board);
            heartRepository.save(heart);

            board.setLike_count(board.getLike_count() + 1);//게시물의 좋아요수 증가
            boardRepository.save(board);
            return true;
        }else{
            System.out.println("이미 좋아요 눌렀어요(삭제처리드갑니데이)");

            // 이미 좋아요를 누른 경우, 해당 좋아요를 제거합니다.
            Optional<Heart> existingHeart = heartRepository.findByUserAndBoard(user, board);
            System.out.println("이미 좋아요를 눌렀습니다. 정보 : " + existingHeart);

            if (existingHeart != null) {
                System.out.println("삭제처리의 if문으로 들어왔다.");
                heartRepository.deleteByUserAndBoard(user, board);
                System.out.println("하트테이블에서 삭제완료");
                board.setLike_count(board.getLike_count() - 1); // 게시물의 좋아요수 감소
                boardRepository.save(board);
                System.out.println("보드테이블에서 -1 완료");
            }

            return false; // 이미 좋아요를 누른 경우
        }
    }

//    로그인한 사용자가 해당 글에 하트를 눌렀는지 확인 하기 위한 메서드(true/false)
    public boolean hasUserLikedPost(User user, Board board) {
        return heartRepository.existsByUserAndBoard(user, board);
    }

}

