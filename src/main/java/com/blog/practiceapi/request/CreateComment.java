package com.blog.practiceapi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CreateComment {

    @Length(min = 1, max = 10, message = "작성자 제한 1~10")
    @NotBlank
    private String author;

    @Length(min = 1, max = 6, message = "비밀번호 제한 1~6")
    @NotBlank
    private String password;

    @Length(min = 1, max = 800, message = "내용 제한 1~800")
    @NotBlank
    private String content;

    @Builder
    public CreateComment(String author, String password, String content) {
        this.author = author;
        this.password = password;
        this.content = content;
    }
}
