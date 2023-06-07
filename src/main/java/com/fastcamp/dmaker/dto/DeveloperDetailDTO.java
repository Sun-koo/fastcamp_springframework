package com.fastcamp.dmaker.dto;

import com.fastcamp.dmaker.entity.Developer;
import com.fastcamp.dmaker.type.DeveloperLevel;
import com.fastcamp.dmaker.type.DeveloperSkillType;
import com.fastcamp.dmaker.type.StatusCode;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDetailDTO {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private Integer experienceYears;
    private String memberId;
    private String name;
    private Integer age;
    private StatusCode statusCode;

    public static DeveloperDetailDTO fromEntity(Developer developer) {
        return DeveloperDetailDTO.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .experienceYears(developer.getExperienceYears())
                .memberId(developer.getMemberId())
                .name(developer.getName())
                .age(developer.getAge())
                .statusCode(developer.getStatusCode())
                .build();
    }
}
