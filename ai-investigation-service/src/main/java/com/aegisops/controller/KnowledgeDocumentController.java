package com.aegisops.controller;

import com.aegisops.service.KnowledgeDocumentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeDocumentController {

    private final KnowledgeDocumentService knowledgeDocumentService;

    public KnowledgeDocumentController(
            KnowledgeDocumentService knowledgeDocumentService) {

        this.knowledgeDocumentService = knowledgeDocumentService;
    }

    @PostMapping
    public String addDocument(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String source,
            @RequestParam String documentType) {

        knowledgeDocumentService.addDocument(
                title,
                content,
                source,
                documentType
        );

        return "Knowledge document added successfully.";
    }
}