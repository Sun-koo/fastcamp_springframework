package com.fastcamp.dmaker.controller;

import com.fastcamp.dmaker.dto.CreateDeveloper;
import com.fastcamp.dmaker.dto.DeveloperDTO;
import com.fastcamp.dmaker.dto.DeveloperDetailDTO;
import com.fastcamp.dmaker.dto.UpdateDeveloper;
import com.fastcamp.dmaker.service.DMakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDTO> getAllDevelopers() {
        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDTO getDeveloperDetail(
            @PathVariable final String memberId
    ) {
        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDeveloper(
            @Valid @RequestBody final CreateDeveloper.Request request
    ) {
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);
    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDTO updateDeveloper(
            @PathVariable final String memberId,
            @Valid @RequestBody final UpdateDeveloper.Request request
    ) {
        return dMakerService.updateDeveloper(memberId, request);
    }

    @DeleteMapping("developer/{memberId}")
    public DeveloperDetailDTO deleteDeveloper(
            @PathVariable final String memberId
    ) {
        return dMakerService.deleteDeveloper(memberId);
    }
}
