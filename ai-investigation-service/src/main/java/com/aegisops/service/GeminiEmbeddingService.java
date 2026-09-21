package com.aegisops.service;

import com.google.genai.Client;
import com.google.genai.types.ContentEmbedding;
import com.google.genai.types.EmbedContentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeminiEmbeddingService {

    private final Client geminiClient;

    public GeminiEmbeddingService(Client geminiClient) {
        this.geminiClient = geminiClient;
    }

    public List<Float> generateEmbedding(String text) {

        EmbedContentResponse response =
                geminiClient.models.embedContent(
                        "gemini-embedding-2",
                        text,
                        null
                );

        ContentEmbedding embedding =
                response.embeddings()
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Gemini did not return an embedding."
                                ))
                        .get(0);

        return embedding.values()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Gemini returned an embedding without values."
                        ));
    }
}