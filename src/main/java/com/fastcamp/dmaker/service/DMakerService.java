package com.fastcamp.dmaker.service;

import com.fastcamp.dmaker.entity.Developer;
import com.fastcamp.dmaker.repository.DeveloperRepository;
import com.fastcamp.dmaker.type.DeveloperLevel;
import com.fastcamp.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;

    @Transactional
    public void createDeveloper() {
        Developer developer = Developer.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(2)
                .name("Olaf")
                .age(25)
                .build();

        developerRepository.save(developer);
    }
}
