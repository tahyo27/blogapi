package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Member;
import com.blog.practiceapi.domain.Session;
import com.blog.practiceapi.encryption.PasswordEncryption;
import com.blog.practiceapi.exception.AlreadyExistEmail;
import com.blog.practiceapi.exception.InvalidLogInException;
import com.blog.practiceapi.repository.MemberRepository;
import com.blog.practiceapi.request.Login;
import com.blog.practiceapi.request.Sign;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService{

    private final MemberRepository memberRepository;
    private final PasswordEncryption passwordEncryption;

    @Override
    public Long login(Login login) {
        Member member = memberRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidLogInException::new);

        boolean matches = passwordEncryption.matches(login.getPassword(), member.getPassword());

        if(!matches) {
            throw new InvalidLogInException();
        }

        return member.getId();
    }

    @Override
    public void sign(Sign sign) {
        Optional<Member> memberOptional = memberRepository.findByEmail(sign.getEmail());
        if(memberOptional.isPresent()) {
            throw new AlreadyExistEmail();
        }

        String scrpytPassword = passwordEncryption.encrypt(sign.getPassword());


        Member member = Member.builder()
                .name(sign.getName())
                .email(sign.getEmail())
                .password(scrpytPassword)
                .build();

        memberRepository.save(member);

    }

}
