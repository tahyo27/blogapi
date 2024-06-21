package com.blog.practiceapi.error;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String msg;

    @JsonProperty("validation")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ValidationError validationError;

}
