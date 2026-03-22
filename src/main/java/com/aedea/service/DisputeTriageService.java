package com.aedea.service;

import com.aedea.domain.TriageAssessment;
import com.aedea.domain.enums.CaveatType;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.domain.enums.guidance.RecommendedAction;
import com.aedea.dto.DisputeTriageRequest;
import com.aedea.dto.DisputeTriageResponse;
import com.aedea.dto.TriageExplanationResponse;
import com.aedea.guidance.GuidanceRetriever;
import com.aedea.guidance.MockGuidanceDocument;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DisputeTriageService {
    private final GuidanceRetriever guidanceRetriever;

    public DisputeTriageService(GuidanceRetriever guidanceRetriever) {
        this.guidanceRetriever = guidanceRetriever;
    }

    public DisputeTriageResponse triage(DisputeTriageRequest request) {
        if (request.getDisputeDate().isBefore(request.getTransactionDate())) {
            throw new IllegalArgumentException("disputeDate must be on or after transactionDate");
        }

        TriageAssessment assessment = buildAssessment(request);
        return toResponse(assessment);
    }

    private TriageAssessment buildAssessment(DisputeTriageRequest request) {
        MockGuidanceDocument guidance = guidanceRetriever.retrieve(request)
            .orElseThrow(() -> new IllegalArgumentException("No guidance available for reason code"));
        List<EvidenceType> providedEvidence = request.getEvidenceProvided() == null
            ? Collections.emptyList()
            : request.getEvidenceProvided();
        List<EvidenceType> missingEvidence = new ArrayList<>();
        for (EvidenceType evidenceType : guidance.getExpectedEvidence()) {
            if (!providedEvidence.contains(evidenceType)) {
                missingEvidence.add(evidenceType);
            }
        }

        List<CaveatType> caveats = new ArrayList<>();

        String merchantNarrative = request.getMerchantNarrative();
        if (merchantNarrative == null || merchantNarrative.isBlank()) {
            caveats.add(CaveatType.MISSING_MERCHANT_NARRATIVE);
        }

        if (request.getTransactionDate() != null && request.getDisputeDate() != null) {
            long daysBetween = ChronoUnit.DAYS.between(request.getTransactionDate(), request.getDisputeDate());
            if (daysBetween > 60) {
                caveats.add(CaveatType.LATE_DISPUTE);
            }
        }

        boolean hasMissingEvidence = !missingEvidence.isEmpty();

        TriageAssessment assessment = new TriageAssessment();
        assessment.setMatchedGuidanceTitle(guidance.getTitle());
        assessment.setGuidanceMatched(true);
        assessment.setGuidanceSource("mock-catalog");
        assessment.setExpectedEvidence(guidance.getExpectedEvidence());
        assessment.setSupportingGuidanceNotes(guidance.getOperationalNotes());
        assessment.setSummaryType(guidance.getSummaryType());
        assessment.setRecommendedAction(hasMissingEvidence
            ? guidance.getRecommendedAction()
            : RecommendedAction.REVIEW_CASE_READINESS);
        assessment.setActionReason(hasMissingEvidence
            ? "Dispute-type guidance action selected because expected evidence is missing."
            : "Readiness review selected because all expected evidence is present.");
        assessment.setMissingEvidence(missingEvidence);
        assessment.setCaveatTypes(caveats);
        assessment.setGuidanceDocumentId(guidance.getDocumentId());
        return assessment;
    }

    private DisputeTriageResponse toResponse(TriageAssessment assessment) {
        DisputeTriageResponse response = new DisputeTriageResponse();
        response.setLikelyMissingEvidence(assessment.getMissingEvidence());
        response.setCaveats(assessment.getCaveatTypes());
        response.setSummary(assessment.getSummaryType().getMessage());
        response.setNextRecommendedAction(assessment.getRecommendedAction().getMessage());
        response.setTriageExplanation(toExplanationResponse(assessment));
        response.setSupportingGuidanceNotes(assessment.getSupportingGuidanceNotes());
        return response;
    }

    private TriageExplanationResponse toExplanationResponse(TriageAssessment assessment) {
        TriageExplanationResponse explanation = new TriageExplanationResponse();
        explanation.setGuidanceMatched(assessment.isGuidanceMatched());
        explanation.setMatchedGuidanceTitle(assessment.getMatchedGuidanceTitle());
        explanation.setGuidanceSource(assessment.getGuidanceSource());
        explanation.setActionReason(assessment.getActionReason());
        return explanation;
    }

}
