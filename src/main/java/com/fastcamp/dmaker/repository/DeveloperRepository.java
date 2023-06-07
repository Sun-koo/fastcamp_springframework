package com.fastcamp.dmaker.repository;

import com.fastcamp.dmaker.entity.Developer;
import com.fastcamp.dmaker.type.StatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Optional<Developer> findByMemberId(String memberId);
    List<Developer> findDevelopersByStatusCodeEquals(StatusCode statusCode);
}
