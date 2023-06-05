package com.fastcamp.dmaker.controller;

import com.fastcamp.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<String> getDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return Arrays.asList("Snow", "Elsa", "Olaf");
    }

    @GetMapping("/create-developer")
    public List<String> createDeveloper() {
        dMakerService.createDeveloper();

        return List.of("Olaf");
    }
}
