package com.fastcamp.dmaker.repository;

import com.fastcamp.dmaker.entity.RetiredDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetiredDeveloperRepository extends JpaRepository<RetiredDeveloper, Long> {
    Optional<RetiredDeveloper> findByMemberId(String memberId);
}
