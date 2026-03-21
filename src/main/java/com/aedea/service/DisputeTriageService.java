package com.aedea.service;

import com.aedea.domain.MockDisputeGuidance;
import com.aedea.domain.enums.CaveatType;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.domain.enums.guidance.RecommendedAction;
import com.aedea.dto.DisputeTriageRequest;
import com.aedea.dto.DisputeTriageResponse;
import com.aedea.guidance.MockGuidanceCatalog;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DisputeTriageService {
    private final MockGuidanceCatalog guidanceCatalog = new MockGuidanceCatalog();

    public DisputeTriageResponse triage(DisputeTriageRequest request) {
        if (request.getDisputeDate().isBefore(request.getTransactionDate())) {
            throw new IllegalArgumentException("disputeDate must be on or after transactionDate");
        }

        MockDisputeGuidance guidance = guidanceCatalog.getGuidance(request.getReasonCode());
        List<EvidenceType> providedEvidence = request.getEvidenceProvided() == null
            ? Collections.emptyList()
            : request.getEvidenceProvided();
        List<EvidenceType> missingEvidence = new ArrayList<>();
        for (EvidenceType evidenceType : guidance.getRecommendedEvidence()) {
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

        DisputeTriageResponse response = new DisputeTriageResponse();
        response.setLikelyMissingEvidence(missingEvidence);
        response.setCaveats(caveats);
        response.setSummary(guidance.getGuidanceSummary().getMessage());
        response.setNextRecommendedAction(missingEvidence.isEmpty()
            ? RecommendedAction.REVIEW_CASE_READINESS.getMessage()
            : guidance.getRecommendedAction().getMessage());

        return response;
    }
}
