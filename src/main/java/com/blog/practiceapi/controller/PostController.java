package com.blog.practiceapi.controller;

import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.CursorPaging;
import com.blog.practiceapi.request.EditPost;
import com.blog.practiceapi.request.OffsetPaging;
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
        postService.write(postRequest);
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
        if(file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 업로드되지 않았습니다");
        } else if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            return ResponseEntity.badRequest().body("파일의 이름이 잘못되었습니다");
        }

        Path tempDirPath = Path.of("./temp/image");
        try {
            if (!Files.exists(tempDirPath)) {
                Files.createDirectories(tempDirPath);
            }
            String originName = file.getOriginalFilename();
            String uuidName = UUID.randomUUID().toString();
            String tempName = uuidName + "_" + originName;
            Path tempFilePath = tempDirPath.resolve(tempName); // 패스 합치기
            //temp에 이미지 저장
            Files.copy(file.getInputStream(), tempFilePath);

            String imageUrl = "/temp/image/" + tempName; // 이미지 받을 주소

            return ResponseEntity.ok().body(Map.of("url", imageUrl));

        } catch (IOException e) {
            return ResponseEntity.status(500).body("이미지 업로드 실패");
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/temp/image/{filename}") //임시 저장한 이미지 에디터로 보내는 주소
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path file = Path.of("./temp/image", filename); // 임시 저장 이미지파일 경로
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
