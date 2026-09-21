package com.aegisops.controller;

import com.aegisops.service.GeminiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/test")
    public String testGemini(@RequestParam String prompt) {
        return geminiService.generateInvestigation(prompt);
    }
}