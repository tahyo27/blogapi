package com.blog.practiceapi.service;

import com.blog.practiceapi.repository.MemberRepository;
import com.blog.practiceapi.request.Sign;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService{

    private final MemberRepository memberRepository;


    @Override
    public void sign(Sign sign) {


    }

}
