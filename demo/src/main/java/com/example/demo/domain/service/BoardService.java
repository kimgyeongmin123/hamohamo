package com.example.demo.domain.service;

import com.example.demo.domain.dto.BoardDto;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    private String uploadDir = "c:\\upload";

    @Autowired
    private BoardRepository boardRepository;

    @Transactional(rollbackFor = SQLException.class)
    public boolean addBoard(@ModelAttribute("boardDto") BoardDto dto) throws IOException {

        Board board = new Board();
        board.setId(dto.getId());
        board.setContents(dto.getContents());
        board.setDate(LocalDateTime.now());
        board.setHits(0L);
        board.setLike_count(0L);

        board = boardRepository.save(board);
        System.out.println("board : "+board);

        //MultipartFile 업로드한 파일을 배열로 가져옴.
        MultipartFile [] files = dto.getFiles();

        List<String> filenames = new ArrayList<String>();
        List<String> filesizes = new ArrayList<String>();

        System.out.println("filenames : "+filenames);
        System.out.println("filesizes : "+filesizes);

        if(dto.getFiles().length>=1 && dto.getFiles()[0].getSize()!=0L){
            //Upload Dir 미존재시 생성
            String path = uploadDir+ File.separator+dto.getId()+File.separator+ UUID.randomUUID();
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
            }
            board.setFilename(filenames.toString());
            board.setFilesize(filesizes.toString());
        }


        boolean issaved =  boardRepository.existsByNumber(board.getNumber());
        System.out.println("issaved : "+issaved);
        return issaved;
    }

}
