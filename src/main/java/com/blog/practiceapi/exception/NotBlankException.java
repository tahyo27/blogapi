package com.blog.practiceapi.exception;

import com.blog.practiceapi.response.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class NotBlankException extends BlogException{

    private static final String MESSAGE = "값을 입력하지 않았습니다";

    public NotBlankException() {
        super(MESSAGE);
    }

    public NotBlankException(MethodArgumentNotValidException e) {
        super(MESSAGE);

        Map<String, String> fieldErrors = e.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        objectError -> ((FieldError) objectError).getField(),
                        objectError -> objectError.getDefaultMessage()
                ));
        log.info("낫블랭크 익셉션 >>>>>>>>{}", fieldErrors.toString());
        addValidation(new ValidationError(fieldErrors));
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
