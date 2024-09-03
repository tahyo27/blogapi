package com.blog.practiceapi.service;

import com.blog.practiceapi.common.GoogleStorageUtil;
import com.blog.practiceapi.common.ImageNameParser;
import com.blog.practiceapi.common.ImageProcess;
import com.blog.practiceapi.domain.Image;
import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.domain.PostEditor;
import com.blog.practiceapi.exception.ImageUploadException;
import com.blog.practiceapi.exception.PostNotFound;
import com.blog.practiceapi.repository.ImageRepository;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.CursorPaging;
import com.blog.practiceapi.request.EditPost;
import com.blog.practiceapi.request.OffsetPaging;
import com.blog.practiceapi.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final GoogleStorageUtil googleStorageUtil;
    private final ImageRepository imageRepository;



    @Override
    @Transactional
    public void write(CreatePost createPost, List<ImageNameParser> parserList) {
        Post post = Post.builder()
                .title(createPost.getTitle())
                .content(createPost.getContent())
                .build();

        postRepository.save(post);
        log.info("wrtie parserList >>>>>>>>>>>>>> {}", parserList);
        if(!parserList.isEmpty()) {
            uploadImage(parserList, post);
        }
    }

    @Override
    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Override
    public List<PostResponse> getList(OffsetPaging search) {
        return postRepository.getPagingList(search).stream().map
                (PostResponse::new)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void editPost(Long id, EditPost editPost) {
        Post post = postRepository.findById(id) //포스트 존재하는지 확인
                .orElseThrow(PostNotFound::new);

        List<String> savedList = imageRepository.findImagePathsByPostId(id); // 없어도 상관 X
        
        //이미지 처리부분
        ImageProcess imageProcess  = new ImageProcess(editPost.getContent(), savedList);

        if(!imageProcess.getDeletePath().isEmpty()) { //삭제해야할 부분 삭제 (구글 스토리지에서)
            deleteImages(imageProcess.getDeletePath(), id); //todo jpa 때문에 업로드 post로 했는데 통일 할지 말지 고민
        }

        if(!imageProcess.getImageList().isEmpty()) { //업로드 해야될 부분 업로드 (구글 스토리지에서)
            uploadImage(imageProcess.getImageList(), post);
        }

        PostEditor.PostEditorBuilder builder = post.toPostEditor();

        PostEditor postEditor = builder  //todo 변경 테스트하기
                .title(editPost.getTitle())
                .content(imageProcess.getContent())
                .build();

        post.edit(postEditor);
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(post); // todo 삭제했을때 삭제부분 처리하기 작성 필요
    }

    @Override
    public List<PostResponse> getListWithCursor(CursorPaging cursorPaging) {
        return postRepository.getCursorPaging(cursorPaging).stream()
                .map(PostResponse::new)
                .toList();
    }

    @Override
    public List<String> getImagePath(Long postId) {
        return imageRepository.findImagePathsByPostId(postId);
    }

    private void uploadImage(List<ImageNameParser> parserList, Post post) {
        List<Image> imageList = new ArrayList<>();
        for (ImageNameParser imageNameParser : parserList) {
            if (googleStorageUtil.imgUpload(imageNameParser)) {
                Image image = imageNameParser.convertImage(post);
                imageList.add(image);
            } else {
                throw new ImageUploadException();
            }
        }
        imageRepository.saveAll(imageList);
    }

    private void deleteImages(List<String> deletePath, Long postId) { //todo 예외처리
        for (String str : deletePath) {
            googleStorageUtil.imgDelete(str);
        }
        imageRepository.deleteByPostIdAndImagePathIn(postId, deletePath);
    }


}
