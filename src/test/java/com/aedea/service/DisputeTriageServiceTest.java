package com.aedea.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aedea.domain.enums.CardScheme;
import com.aedea.domain.enums.DisputeReasonCode;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.domain.enums.guidance.RecommendedAction;
import com.aedea.dto.DisputeTriageRequest;
import com.aedea.dto.DisputeTriageResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class DisputeTriageServiceTest {

    private static final LocalDate TRANSACTION_DATE = LocalDate.of(2025, 1, 10);
    private static final LocalDate DISPUTE_DATE = LocalDate.of(2025, 1, 12);
    private static final String REVIEW_CASE_READINESS_MESSAGE =
        "Review available evidence and decide whether the case is ready to contest";

    private final DisputeTriageService service = new DisputeTriageService();

    @Test
    void throwsWhenDisputeDateIsBeforeTransactionDate() {
        DisputeTriageRequest request = requestFor(DisputeReasonCode.GOODS_NOT_RECEIVED);
        request.setTransactionDate(DISPUTE_DATE);
        request.setDisputeDate(TRANSACTION_DATE);

        assertThrows(IllegalArgumentException.class, () -> service.triage(request));
    }

    @Test
    void goodsNotReceivedMissingEvidenceIncludesProofOfDelivery() {
        DisputeTriageResponse response = triage(DisputeReasonCode.GOODS_NOT_RECEIVED,
            EvidenceType.CUSTOMER_COMMUNICATION);

        assertTrue(response.getLikelyMissingEvidence().contains(EvidenceType.PROOF_OF_DELIVERY));
    }

    @Test
    void goodsNotReceivedWithAllEvidenceHasNoMissingEvidenceAndReviewAction() {
        DisputeTriageResponse response = triage(DisputeReasonCode.GOODS_NOT_RECEIVED,
            EvidenceType.PROOF_OF_DELIVERY, EvidenceType.CUSTOMER_COMMUNICATION);

        assertTrue(response.getLikelyMissingEvidence().isEmpty());
        assertEquals(REVIEW_CASE_READINESS_MESSAGE, response.getNextRecommendedAction());
    }

    @Test
    void serviceNotProvidedMissingEvidenceIncludesServiceLog() {
        DisputeTriageResponse response = triage(DisputeReasonCode.SERVICE_NOT_PROVIDED,
            EvidenceType.CUSTOMER_COMMUNICATION);

        assertTrue(response.getLikelyMissingEvidence().contains(EvidenceType.SERVICE_LOG));
        assertTrue(response.getNextRecommendedAction().contains(RecommendedAction.REVIEW_SERVICE_RECORDS.getMessage()));
    }

    @Test
    void duplicateProcessingUsesGuidanceRecommendationWhenEvidenceMissing() {
        DisputeTriageResponse response = triage(DisputeReasonCode.DUPLICATE_PROCESSING,
            EvidenceType.CUSTOMER_COMMUNICATION);

        assertEquals(RecommendedAction.CHECK_FOR_DUPLICATE_CHARGE.getMessage(), response.getNextRecommendedAction());
    }

    private DisputeTriageResponse triage(DisputeReasonCode reasonCode, EvidenceType... evidenceProvided) {
        return service.triage(requestFor(reasonCode, evidenceProvided));
    }

    private DisputeTriageRequest requestFor(DisputeReasonCode reasonCode, EvidenceType... evidenceProvided) {
        DisputeTriageRequest request = new DisputeTriageRequest();
        request.setScheme(CardScheme.VISA);
        request.setReasonCode(reasonCode);
        request.setAmount(new BigDecimal("12.50"));
        request.setTransactionDate(TRANSACTION_DATE);
        request.setDisputeDate(DISPUTE_DATE);
        if (evidenceProvided.length > 0) {
            request.setEvidenceProvided(List.of(evidenceProvided));
        }
        return request;
    }
}
