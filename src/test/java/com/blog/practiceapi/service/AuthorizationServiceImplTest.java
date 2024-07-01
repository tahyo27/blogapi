package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Member;
import com.blog.practiceapi.encryption.PasswordEncryption;
import com.blog.practiceapi.exception.InvalidLogInException;
import com.blog.practiceapi.exception.PostNotFound;
import com.blog.practiceapi.repository.MemberRepository;

import com.blog.practiceapi.request.Login;
import com.blog.practiceapi.request.Sign;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


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
        Member findMeber = memberRepository.findById(member.getId()).orElseThrow(() -> new NullPointerException());


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

    @Test
    @DisplayName("회원 가입 암호화 테스트")
    void member_sign_scrypt_test() {
        //given
        Sign sign = Sign.builder()
                .name("HONG")
                .password("1234")
                .email("aaa@naver.com")
                .build();
        //when
        authorizationService.sign(sign);
        Member member = memberRepository.findByEmail(sign.getEmail()).orElseThrow(() -> new PostNotFound());

        //then
        Assertions.assertThat(sign.getEmail()).isEqualTo(member.getEmail());
        Assertions.assertThat(sign.getName()).isEqualTo(member.getName());
        assertTrue(new PasswordEncryption().matches("1234", member.getPassword()));


    }

    @Test
    @DisplayName("비밀번호 암호화 로그인 테스트")
    void member_login_scrypt_test() {
        //given
        Member member = Member.builder()
                .name("psyduck")
                .email("aaaa@naver.com")
                .password(new PasswordEncryption().encrypt("1234"))
                .build();

        memberRepository.save(member);

        Login login = Login.builder()
                .email("aaaa@naver.com")
                .password("1234")
                .build();

        //when
        Long memberId = authorizationService.login(login);

        //then
        Assertions.assertThat(memberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("비밀번호 암호화 로그인 실패 테스트")
    void member_login_scrypt_fail_test() {
        //given
        Member member = Member.builder()
                .name("psyduck")
                .email("aaaa@naver.com")
                .password(new PasswordEncryption().encrypt("1234"))
                .build();

        memberRepository.save(member);

        Login login = Login.builder()
                .email("aaa@naver.com")
                .password("3456")
                .build();

        //expected
        assertThrows(InvalidLogInException.class, () -> authorizationService.login(login));
    }
}