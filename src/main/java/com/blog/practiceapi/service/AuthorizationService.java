package com.blog.practiceapi.service;

import com.blog.practiceapi.request.Login;
import com.blog.practiceapi.request.Sign;

public interface AuthorizationService {
    Long login(Login login);

    void sign(Sign sign);
}
