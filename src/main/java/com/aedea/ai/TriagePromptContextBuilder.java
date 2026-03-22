package com.aedea.ai;

import com.aedea.domain.TriageAssessment;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.dto.DisputeTriageRequest;
import com.aedea.guidance.MockGuidanceDocument;
import java.util.List;

public class TriagePromptContextBuilder {

    public TriagePromptContext build(
        DisputeTriageRequest request,
        TriageAssessment assessment,
        MockGuidanceDocument guidance
    ) {
        TriagePromptContext context = new TriagePromptContext();
        context.setScheme(request.getScheme());
        context.setReasonCode(request.getReasonCode());
        context.setAmount(request.getAmount());
        context.setTransactionDate(request.getTransactionDate());
        context.setDisputeDate(request.getDisputeDate());
        context.setMerchantNarrative(request.getMerchantNarrative());
        context.setEvidenceProvided(safeEvidence(request.getEvidenceProvided()));
        context.setMatchedGuidanceTitle(assessment.getMatchedGuidanceTitle());
        context.setSupportingGuidanceNotes(assessment.getSupportingGuidanceNotes());
        context.setMissingEvidence(assessment.getMissingEvidence());
        context.setRecommendedAction(assessment.getRecommendedAction());
        context.setCaveats(assessment.getCaveatTypes());
        return context;
    }

    private List<EvidenceType> safeEvidence(List<EvidenceType> evidence) {
        return evidence == null ? List.of() : evidence;
    }
}
