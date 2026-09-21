package com.aegisops.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final Client geminiClient;
    private final String model;

    public GeminiService(
            Client geminiClient,
            @Value("${gemini.model}") String model) {

        this.geminiClient = geminiClient;
        this.model = model;
    }

    public String generateInvestigation(String prompt) {

        Schema responseSchema =
                Schema.builder()
                        .type(Type.Known.OBJECT)
                        .properties(
                                Map.of(
                                        "summary",
                                        Schema.builder()
                                                .type(Type.Known.STRING)
                                                .description(
                                                        "A concise summary of the security incident."
                                                )
                                                .build(),

                                        "findings",
                                        Schema.builder()
                                                .type(Type.Known.STRING)
                                                .description(
                                                        "Evidence-based findings from the incident information."
                                                )
                                                .build(),

                                        "recommendedAction",
                                        Schema.builder()
                                                .type(Type.Known.STRING)
                                                .description(
                                                        "A recommended security response. Do not execute the action."
                                                )
                                                .build()
                                )
                        )
                        .required(
                                List.of(
                                        "summary",
                                        "findings",
                                        "recommendedAction"
                                )
                        )
                        .build();

        GenerateContentConfig config =
                GenerateContentConfig.builder()
                        .responseMimeType("application/json")
                        .responseSchema(responseSchema)
                        .candidateCount(1)
                        .build();

        GenerateContentResponse response =
                geminiClient.models.generateContent(
                        model,
                        prompt,
                        config
                );

        return response.text();
    }
}