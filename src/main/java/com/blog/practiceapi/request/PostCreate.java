package com.blog.practiceapi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank //검증
    String title;
    
    @NotBlank
    String content;

}
