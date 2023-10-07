package com.example.demo.domain.service;

import com.example.demo.controller.BoardController;
import com.example.demo.domain.dto.BoardDto;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.repository.BoardRepository;
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



    private String uploadDir = "c://";

    @Autowired
    private BoardRepository boardRepository;



    @Transactional(rollbackFor = SQLException.class)
    public boolean addBoard(BoardDto dto) throws IOException {
        System.out.println("upload File Count : " +dto.getFiles().length);

        Board board = new Board();
        board.setEmail(dto.getEmail());
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
            String path = uploadDir+ File.separator+dto.getEmail()+File.separator+ UUID.randomUUID();
            File dir = new File(path);
            if(!dir.exists()) {
                dir.mkdirs();
            }
            //board에 경로 추가
            board.setDirpath(dir.toString());


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

                //섬네일이미지 파일 만들기

                File thumbnailFile = new File(path, "s_" + filename);

                BufferedImage bo_img = ImageIO.read(fileobj);
                double ratio = 3;
                int width = (int) (bo_img.getWidth() / ratio);
                int height = (int) (bo_img.getHeight() / ratio);

                Thumbnails.of(fileobj)
                        .size(width, height)
                        .toFile(thumbnailFile);
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

    @Transactional(rollbackFor = SQLException.class)
    public void likeBoard(Long number) {
        Board board = boardRepository.findByNum(number).orElse(null);
        if (board != null) {
            board.setLike_count(board.getLike_count() + 1);
            boardRepository.save(board);
        }
    }

}

