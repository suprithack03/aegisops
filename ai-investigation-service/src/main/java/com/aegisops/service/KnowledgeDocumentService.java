package com.aegisops.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeDocumentService {

    private final JdbcTemplate jdbcTemplate;
    private final GeminiEmbeddingService embeddingService;

    public KnowledgeDocumentService(
            JdbcTemplate jdbcTemplate,
            GeminiEmbeddingService embeddingService) {

        this.jdbcTemplate = jdbcTemplate;
        this.embeddingService = embeddingService;
    }

    public void addDocument(
            String title,
            String content,
            String source,
            String documentType) {

        List<Float> embedding =
                embeddingService.generateEmbedding(content);

        String vectorValue = toVectorString(embedding);

        jdbcTemplate.update(
                """
                INSERT INTO knowledge_documents
                    (title, content, source, document_type, embedding)
                VALUES
                    (?, ?, ?, ?, ?::vector)
                """,
                title,
                content,
                source,
                documentType,
                vectorValue
        );
    }

    private String toVectorString(List<Float> embedding) {

        return embedding.toString();
    }
}