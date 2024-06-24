package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.EditPost;
import com.blog.practiceapi.request.SearchPagingPost;
import com.blog.practiceapi.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public void write(CreatePost createPost) {
        Post post = Post.builder()
                .title(createPost.getTitle())
                .content(createPost.getContent())
                .build();

        postRepository.save(post);
    }

    @Override
    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않음"));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Override
    public List<PostResponse> getList(SearchPagingPost search) {
        return postRepository.getPagingList(search).stream().map
                (items -> new PostResponse(items))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void editPost(Long id, EditPost editPost) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));
        post.changePost(editPost.getTitle(), editPost.getContent());
        log.info("post={}", post);
    }


}
