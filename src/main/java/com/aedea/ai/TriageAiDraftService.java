package com.aedea.ai;

import com.aedea.dto.DisputeTriageRequest;
import com.aedea.service.DisputeTriageService;
import org.springframework.stereotype.Service;

@Service
public class TriageAiDraftService {
    private final DisputeTriageService disputeTriageService;
    private final AiDraftGenerator aiDraftGenerator;

    public TriageAiDraftService(DisputeTriageService disputeTriageService, AiDraftGenerator aiDraftGenerator) {
        this.disputeTriageService = disputeTriageService;
        this.aiDraftGenerator = aiDraftGenerator;
    }

    public String generateDraft(DisputeTriageRequest request) {
        String prompt = disputeTriageService.buildPromptPreview(request);
        return aiDraftGenerator.generateDraft(prompt);
    }
}
