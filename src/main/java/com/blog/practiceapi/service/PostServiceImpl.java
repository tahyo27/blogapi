package com.blog.practiceapi.service;

import com.blog.practiceapi.common.GoogleStorageUtil;
import com.blog.practiceapi.common.ImageNameParser;
import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.domain.PostEditor;
import com.blog.practiceapi.exception.PostNotFound;
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

    @Override
    public void write(CreatePost createPost, List<ImageNameParser> parserList) {
        Post post = Post.builder()
                .title(createPost.getTitle())
                .content(createPost.getContent())
                .build();

        postRepository.save(post);
// todo 완성하기
//        if(!parserList.isEmpty()) {
//            uploadImage(parserList, post.getId());
//        }
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
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder builder = post.toPostEditor();
        PostEditor postEditor = builder
                .title(editPost.getTitle())
                .content(editPost.getContent())
                .build();

        post.edit(postEditor);
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }

    @Override
    public List<PostResponse> getListWithCursor(CursorPaging cursorPaging) {
        return postRepository.getCursorPaging(cursorPaging).stream()
                .map(PostResponse::new)
                .toList();
    }

    /*private void uploadImage(List<ImageNameParser> parserList, Long postId) {
        List<Image> imageList = new ArrayList<>();
        for (ImageNameParser imageNameParser : parserList) {
            if (googleStorageUtil.imgUpload(imageNameParser)) {
                Image image = imageNameParser.convertImage(postId);
                imageList.add(image);
            } else {
                log.info("예외처리 들어가야함");
            }
        }
        imagesRepository.saveAll(imageList);
    }*/


}
