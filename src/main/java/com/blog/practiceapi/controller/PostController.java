package com.blog.practiceapi.controller;

import com.blog.practiceapi.common.ImageProcess;
import com.blog.practiceapi.exception.InvalidFileException;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.CursorPaging;
import com.blog.practiceapi.request.EditPost;
import com.blog.practiceapi.response.PostResponse;
import com.blog.practiceapi.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping("/posts")
    public void writePost(@RequestBody @Valid CreatePost postRequest) throws Exception {
        postRequest.isValid();
        
        ImageProcess imageProcess = new ImageProcess(postRequest.getContent()); //이미지 처리
        postRequest.setContent(imageProcess.getContent()); // 주소부분 구글로 변경하는 처리


        postService.write(postRequest); // todo 처리한 이미지부분 서비스에서 이미지 리스트와 딜리트 리스트로 처리 필요
    }

    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable(name = "postId") Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(CursorPaging cursorPaging) {

        return postService.getListWithCursor(cursorPaging);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable(name = "postId") Long postId, @RequestBody @Valid EditPost editRequest) {
        postService.editPost(postId, editRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable(name = "postId") Long postId) {
        postService.delete(postId);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/temp/image")
    public ResponseEntity<?> imageTemp(MultipartFile file) { //에디터 이미지 임시저장
        if(file.isEmpty()) { //파일 체크
            throw new InvalidFileException();
        } else if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new InvalidFileException();
        }

        Path tempDirPath = Path.of("./temp/image"); //파일 경로

        try {
            if (!Files.exists(tempDirPath)) { //디렉토리 없으면 디렉토리 생성
                Files.createDirectories(tempDirPath);
            }
            String originName = file.getOriginalFilename();
            String uuidName = UUID.randomUUID().toString();
            String tempName = uuidName + "_" + originName;
            Path tempFilePath = tempDirPath.resolve(tempName); // 패스 합치기
            //temp에 이미지 저장
            Files.copy(file.getInputStream(), tempFilePath); // 파일 복사

            String imageUrl = "/temp/image/" + tempName; // 이미지 받을 주소

            return ResponseEntity.ok().body(Map.of("url", imageUrl)); // 이미지 url 내려보내주기

        } catch (IOException e) {
            throw new InvalidFileException();
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/temp/image/{filename}") //임시 저장한 이미지 에디터로 보내는 주소
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path file = Path.of("./temp/image", filename); // 임시 저장한 이미지파일 경로
        
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) { //파일이 있으면
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
