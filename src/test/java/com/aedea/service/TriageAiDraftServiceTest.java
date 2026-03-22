package com.aedea.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aedea.ai.AiDraftGenerator;
import com.aedea.dto.DisputeTriageRequest;
import org.junit.jupiter.api.Test;

class TriageAiDraftServiceTest {

    @Test
    void generateDraftUsesPromptFromTriageService() {
        DisputeTriageService triageService = new StubTriageService("Prompt text");
        AiDraftGenerator generator = prompt -> "Draft for: " + prompt;
        TriageAiDraftService service = new TriageAiDraftService(triageService, generator);

        String draft = service.generateDraft(new DisputeTriageRequest());

        assertEquals("Draft for: Prompt text", draft);
    }

    private static final class StubTriageService extends DisputeTriageService {
        private final String prompt;

        private StubTriageService(String prompt) {
            super(request -> java.util.Optional.empty());
            this.prompt = prompt;
        }

        @Override
        public String buildPromptPreview(DisputeTriageRequest request) {
            return prompt;
        }
    }
}
