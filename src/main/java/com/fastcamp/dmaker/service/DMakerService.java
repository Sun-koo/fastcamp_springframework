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
import jakarta.validation.constraints.NotNull;
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

    @Transactional(readOnly = true)
    public List<DeveloperDTO> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeveloperDetailDTO getDeveloperDetail(String memberId) {
        return DeveloperDetailDTO.fromEntity(getDeveloperByMemberId(memberId));
    }

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        Developer developer = request.getEntity();
        developerRepository.save(developer);

        return CreateDeveloper.Response.fromEntity(developer);
    }

    @Transactional
    public DeveloperDetailDTO updateDeveloper(String memberId, UpdateDeveloper.Request request) {
        request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());

        return DeveloperDetailDTO.fromEntity(
                getUpdateDeveloperFromRequest(request, getDeveloperByMemberId(memberId))
        );
    }

    private Developer getUpdateDeveloperFromRequest(UpdateDeveloper.Request request, Developer developer) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return developer;
    }

    @Transactional
    public DeveloperDetailDTO deleteDeveloper(String memberId) {
        // Employed -> Retired
        Developer developer = getDeveloperByMemberId(memberId);
        developer.setStatusCode(StatusCode.RETIRED);

        // Save into RetiredDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(developer.getMemberId())
                .name(developer.getName())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDTO.fromEntity(developer);
    }

    private Developer getDeveloperByMemberId(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    }

    /**
     * Business Validation
     *
     * @param request CreateDeveloper Request Data
     */
    private void validateCreateDeveloperRequest(@NotNull CreateDeveloper.Request request) {
        request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
    }
}
