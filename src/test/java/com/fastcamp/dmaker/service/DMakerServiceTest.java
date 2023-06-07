package com.fastcamp.dmaker.service;

import com.fastcamp.dmaker.dto.CreateDeveloper;
import com.fastcamp.dmaker.dto.DeveloperDetailDTO;
import com.fastcamp.dmaker.entity.Developer;
import com.fastcamp.dmaker.exception.DMakerException;
import com.fastcamp.dmaker.exception.type.DMakerErrorCode;
import com.fastcamp.dmaker.repository.DeveloperRepository;
import com.fastcamp.dmaker.type.DeveloperLevel;
import com.fastcamp.dmaker.type.DeveloperSkillType;
import com.fastcamp.dmaker.type.StatusCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.fastcamp.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.fastcamp.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.fastcamp.dmaker.type.DeveloperLevel.*;
import static com.fastcamp.dmaker.type.DeveloperSkillType.FRONT_END;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private final Developer mockDeveloper = Developer.builder()
            .developerLevel(SENIOR)
            .developerSkillType(FRONT_END)
            .experienceYears(13)
            .statusCode(StatusCode.EMPLOYED)
            .name("name")
            .age(43)
            .build();

    private final CreateDeveloper.Request mockDeveloperRequest =
            getMockDeveloperRequest(SENIOR, FRONT_END, MAX_JUNIOR_EXPERIENCE_YEARS + 1);

    private CreateDeveloper.Request getMockDeveloperRequest(
            DeveloperLevel developerLevel,
            DeveloperSkillType developerSkillType,
            Integer experienceYears
    ) {
        return CreateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .experienceYears(experienceYears)
                .memberId("memberId")
                .name("name")
                .age(43)
                .build();
    }

    @Test
    public void testSomething() {
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(mockDeveloper));

        DeveloperDetailDTO detail = dMakerService.getDeveloperDetail("memberId");

        assertEquals(SENIOR, detail.getDeveloperLevel());
        assertEquals(FRONT_END, detail.getDeveloperSkillType());
        assertEquals(13, detail.getExperienceYears());
    }

    @Test
    public void createDeveloperTestSuccess() {
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);

        dMakerService.createDeveloper(mockDeveloperRequest);

        verify(developerRepository, times(1))
                .save(captor.capture());

        Developer savedDeveloper = captor.getValue();
        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());
    }

    @Test
    public void createDeveloperTestFailWithUnmatchedLevel() {
        DMakerException dMakerException = assertThrows(
                DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getMockDeveloperRequest(JUNIOR, FRONT_END, MAX_JUNIOR_EXPERIENCE_YEARS + 1)
                ));

        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDmakerErrorCode());

        dMakerException = assertThrows(
                DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getMockDeveloperRequest(JUNGNIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS + 1)
                ));

        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDmakerErrorCode());

        dMakerException = assertThrows(
                DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getMockDeveloperRequest(SENIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS - 1)
                ));

        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDmakerErrorCode());
    }

    @Test
    public void createDeveloperTestFailDuplicated() {
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(mockDeveloper));

        DMakerException dMakerException = assertThrows(
                DMakerException.class,
                () -> dMakerService.createDeveloper(mockDeveloperRequest));

        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDmakerErrorCode());
    }
}