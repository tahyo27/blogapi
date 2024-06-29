package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByToken(String token);
}
