package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Member;
import com.blog.practiceapi.exception.AlreadyExistEmail;
import com.blog.practiceapi.repository.MemberRepository;
import com.blog.practiceapi.request.Login;
import com.blog.practiceapi.request.Sign;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService{

    private final MemberRepository memberRepository;

    @Override
    public Long login(Login login) {
        return null;
    }

    @Override
    public void sign(Sign sign) {
        Optional<Member> memberOptional = memberRepository.findByEmail(sign.getEmail());
        if(memberOptional.isPresent()) {
            throw new AlreadyExistEmail();
        }
        Member member = Member.builder()
                .name(sign.getName())
                .email(sign.getEmail())
                .password(sign.getPassword())
                .build();

        memberRepository.save(member);

    }

}
