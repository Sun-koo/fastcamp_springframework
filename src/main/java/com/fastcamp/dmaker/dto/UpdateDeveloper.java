package com.fastcamp.dmaker.dto;

import com.fastcamp.dmaker.type.DeveloperLevel;
import com.fastcamp.dmaker.type.DeveloperSkillType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class UpdateDeveloper {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request {
        @NotNull
        private DeveloperLevel developerLevel;
        @NotNull
        private DeveloperSkillType developerSkillType;
        @NotNull
        @Min(0)
        @Max(20)
        private Integer experienceYears;
    }
//
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class Response {
//        private DeveloperLevel developerLevel;
//        private DeveloperSkillType developerSkillType;
//        private Integer experienceYears;
//        private String memberId;
//
//        public static Response fromEntity(Developer developer) {
//            return Response.builder()
//                    .developerLevel(developer.getDeveloperLevel())
//                    .developerSkillType(developer.getDeveloperSkillType())
//                    .experienceYears(developer.getExperienceYears())
//                    .memberId(developer.getMemberId())
//                    .build();
//        }
//    }
}
