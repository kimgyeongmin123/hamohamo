package com.example.demo.domain.service;

import com.example.demo.domain.dto.BoardDto;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public boolean addBoard(BoardDto dto, MultipartFile[] imageFiles) throws IOException {

        Board board = new Board();
        board.setId(dto.getId());
        board.setContents(dto.getContents());
        board.setDate(LocalDateTime.now());
        board.setHits(0L);
        board.setLike_count(0L);

        board = boardRepository.save(board);
        System.out.println("board : " + board);

        List<String> filenames = new ArrayList<>();
        List<String> filesizes = new ArrayList<>();

        if (imageFiles != null && imageFiles.length > 0) {
            String path = uploadDir + File.separator + dto.getId() + File.separator + UUID.randomUUID();
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            board.setDirpath(dir.toString());

            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    System.out.println("--------------------");
                    System.out.println("FILE NAME : " + imageFile.getOriginalFilename());
                    System.out.println("FILE SIZE : " + imageFile.getSize() + " Byte");
                    System.out.println("--------------------");

                    String filename = imageFile.getOriginalFilename();
                    File fileobj = new File(path, filename);
                    imageFile.transferTo(fileobj);

                    filenames.add(filename);
                    filesizes.add(imageFile.getSize() + "");
                }
            }
            board.setFilename(filenames.toString());
            board.setFilesize(filesizes.toString());
        }

        boolean issaved = boardRepository.existsByNumber(board.getNumber());
        System.out.println("issaved : " + issaved);
        return issaved;
    }
}