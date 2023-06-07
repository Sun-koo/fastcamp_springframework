package com.fastcamp.dmaker.service;

import com.fastcamp.dmaker.dto.CreateDeveloper;
import com.fastcamp.dmaker.dto.DeveloperDTO;
import com.fastcamp.dmaker.dto.DeveloperDetailDTO;
import com.fastcamp.dmaker.dto.UpdateDeveloper;
import com.fastcamp.dmaker.entity.Developer;
import com.fastcamp.dmaker.entity.RetiredDeveloper;
import com.fastcamp.dmaker.exception.DMakerException;
import com.fastcamp.dmaker.repository.DeveloperRepository;
import com.fastcamp.dmaker.repository.RetiredDeveloperRepository;
import com.fastcamp.dmaker.type.DeveloperLevel;
import com.fastcamp.dmaker.type.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.fastcamp.dmaker.exception.type.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    public List<DeveloperDTO> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDTO getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDTO::fromEntity)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    }

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .name(request.getName())
                .age(request.getAge())
                .memberId(request.getMemberId())
                .statusCode(StatusCode.EMPLOYED)
                .build();
        developerRepository.save(developer);

        return CreateDeveloper.Response.fromEntity(developer);
    }

    @Transactional
    public DeveloperDetailDTO updateDeveloper(String memberId, UpdateDeveloper.Request request) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDTO.fromEntity(developer);
    }

    @Transactional
    public DeveloperDetailDTO deleteDeveloper(String memberId) {
        // Employed -> Retired
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
        developer.setStatusCode(StatusCode.RETIRED);

        // Save into RetiredDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(developer.getMemberId())
                .name(developer.getName())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDTO.fromEntity(developer);
    }

    /**
     * Business Validation
     *
     * @param request CreateDeveloper Request Data
     */
    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
    }

    private static void validateDeveloperLevel(DeveloperLevel level, int experienceYears) {
        if (level == DeveloperLevel.SENIOR && experienceYears < 10) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (level == DeveloperLevel.JUNGNIOR && (experienceYears < 4 || experienceYears > 10)) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (level == DeveloperLevel.JUNIOR && experienceYears > 4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }
}
