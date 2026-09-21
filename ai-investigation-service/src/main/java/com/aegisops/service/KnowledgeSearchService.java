package com.aegisops.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KnowledgeSearchService {

    private final JdbcTemplate jdbcTemplate;
    private final GeminiEmbeddingService embeddingService;

    public KnowledgeSearchService(
            JdbcTemplate jdbcTemplate,
            GeminiEmbeddingService embeddingService) {

        this.jdbcTemplate = jdbcTemplate;
        this.embeddingService = embeddingService;
    }

    public List<Map<String, Object>> searchSimilar(
            String query,
            int limit) {

        List<Float> queryEmbedding =
                embeddingService.generateEmbedding(query);

        String vectorValue = queryEmbedding.toString();

        return jdbcTemplate.queryForList(
                """
                SELECT
                    id,
                    title,
                    content,
                    source,
                    document_type,
                    1 - (embedding <=> ?::vector) AS similarity
                FROM knowledge_documents
                WHERE embedding IS NOT NULL
                ORDER BY embedding <=> ?::vector
                LIMIT ?
                """,
                vectorValue,
                vectorValue,
                limit
        );
    }
}