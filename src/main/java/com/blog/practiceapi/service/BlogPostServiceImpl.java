package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.repository.BlogPostRepository;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.SearchPagingPost;
import com.blog.practiceapi.response.BlogPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;

    @Override
    public void write(CreatePost createPost) {
        Post post = Post.builder()
                .title(createPost.getTitle())
                .content(createPost.getContent())
                .build();

        blogPostRepository.save(post);
    }

    @Override
    public BlogPostResponse get(Long id) {
        Post post = blogPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않음"));

        return BlogPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Override
    public List<BlogPostResponse> getList(SearchPagingPost search) {
        return blogPostRepository.getPagingList(search).stream().map
                (items -> new BlogPostResponse(items))
                .collect(Collectors.toList());
    }
}
