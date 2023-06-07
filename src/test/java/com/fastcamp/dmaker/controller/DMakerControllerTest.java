package com.fastcamp.dmaker.controller;

import com.fastcamp.dmaker.dto.DeveloperDTO;
import com.fastcamp.dmaker.service.DMakerService;
import com.fastcamp.dmaker.type.DeveloperLevel;
import com.fastcamp.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DMakerController.class)
class DMakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DMakerService dMakerService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);

    @Test
    public void getAllDevelopers() throws Exception {
        DeveloperDTO developerDTO1 = DeveloperDTO.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.BACK_END)
                .memberId("memberId1")
                .build();
        DeveloperDTO developerDTO2 = DeveloperDTO.builder()
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .memberId("memberId2")
                .build();

        given(dMakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(developerDTO1, developerDTO2));

        mockMvc.perform(get("/developers").contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(
                        jsonPath("$.[0].developerSkillType", is(DeveloperSkillType.BACK_END.name()))
                ).andExpect(
                        jsonPath("$.[0].developerLevel", is(DeveloperLevel.JUNIOR.name()))
                ).andExpect(
                        jsonPath("$.[1].developerSkillType", is(DeveloperSkillType.FRONT_END.name()))
                ).andExpect(
                        jsonPath("$.[1].developerLevel", is(DeveloperLevel.SENIOR.name()))
                );
    }
}