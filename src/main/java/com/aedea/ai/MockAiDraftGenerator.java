package com.aedea.ai;

import java.util.Objects;

public class MockAiDraftGenerator implements AiDraftGenerator {
    @Override
    public String generateDraft(String prompt) {
        String safePrompt = Objects.requireNonNullElse(prompt, "");
        return "Draft response preview:\n" + safePrompt.trim();
    }
}
