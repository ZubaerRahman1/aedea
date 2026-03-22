package com.aedea.ai;

import java.util.Objects;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.ai.provider", havingValue = "mock", matchIfMissing = true)
public class MockAiDraftGenerator implements AiDraftGenerator {
    @Override
    public String generateDraft(String prompt) {
        String safePrompt = Objects.requireNonNullElse(prompt, "");
        return "Draft response preview:\n" + safePrompt.trim();
    }
}
