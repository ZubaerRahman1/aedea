package com.aedea.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "gemini")
public class GeminiDraftGenerator implements AiDraftGenerator {
    private static final String DEFAULT_MODEL = "gemini-2.5-flash";

    private final Client client;

    public GeminiDraftGenerator() {
        this.client = new Client();
    }

    @Override
    public String generateDraft(String prompt) {
        GenerateContentResponse response =
            client.models.generateContent(DEFAULT_MODEL, prompt, null);
        return response.text();
    }
}
