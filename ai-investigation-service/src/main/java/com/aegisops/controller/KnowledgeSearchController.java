package com.aegisops.controller;

import com.aegisops.service.KnowledgeSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeSearchController {

    private final KnowledgeSearchService knowledgeSearchService;

    public KnowledgeSearchController(
            KnowledgeSearchService knowledgeSearchService) {

        this.knowledgeSearchService = knowledgeSearchService;
    }

    @GetMapping("/search")
    public List<Map<String, Object>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "3") int limit) {

        return knowledgeSearchService.searchSimilar(query, limit);
    }
}