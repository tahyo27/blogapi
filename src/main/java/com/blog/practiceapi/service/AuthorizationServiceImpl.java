package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Member;
import com.blog.practiceapi.exception.AlreadyExistEmail;
import com.blog.practiceapi.repository.MemberRepository;
import com.blog.practiceapi.request.Sign;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void sign(Sign sign) {
        Optional<Member> memberOptional = memberRepository.findByEmail(sign.getEmail());
        if(memberOptional.isPresent()) {
            throw new AlreadyExistEmail();
        }

        String encryptedPassword = passwordEncoder.encode(sign.getPassword());
        String role = sign.getEmail().equals("tahyo27@gmail.com") ? "ROLE_ADMIN" : "ROLE_USER";
        Member member = Member.builder()
                .name(sign.getName())
                .email(sign.getEmail())
                .password(encryptedPassword)
                .role(role)
                .build();
        
        memberRepository.save(member);
    }

}
