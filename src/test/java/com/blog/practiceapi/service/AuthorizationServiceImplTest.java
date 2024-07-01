package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Member;
import com.blog.practiceapi.repository.MemberRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AuthorizationServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 DB 저장 테스트")
    void member_save_db_test() {
        //given
        Member member = Member.builder()
                .name("고라파덕")
                .password("1234")
                .email("dddd@naver.com")
                .build();

        memberRepository.save(member);
        //when
        Member findMeber = memberRepository.findById(member.getId()).orElseThrow(NullPointerException::new);


        //then
        Assertions.assertThat(member.getName()).isEqualTo(findMeber.getName());
        Assertions.assertThat(member.getEmail()).isEqualTo(findMeber.getEmail());
        Assertions.assertThat(member.getPassword()).isEqualTo(findMeber.getPassword());

    }

    @Test
    @DisplayName("회원 이메일 찾기 테스트")
    void member_find_email_test() {
        //given
        Member member = Member.builder()
                .name("고라파덕")
                .password("1234")
                .email("dddd@naver.com")
                .build();

        memberRepository.save(member);
        //when
        Member findMeber = memberRepository.findByEmail("dddd@naver.com").orElseThrow(() -> new NullPointerException());


        //then
        Assertions.assertThat(member.getName()).isEqualTo(findMeber.getName());
        Assertions.assertThat(member.getEmail()).isEqualTo(findMeber.getEmail());
        Assertions.assertThat(member.getPassword()).isEqualTo(findMeber.getPassword());

    }


}