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
import com.aedea.guidance.MockGuidanceDocumentCatalog;
import com.aedea.guidance.MockGuidanceDocumentLoader;
import com.aedea.guidance.MockGuidanceRetriever;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

class DisputeTriageServiceTest {

    private static final String REVIEW_CASE_READINESS_MESSAGE =
        "Review available evidence and decide whether the case is ready to contest";

    private final DisputeTriageService service = new DisputeTriageService(
        new MockGuidanceRetriever(new MockGuidanceDocumentCatalog(new MockGuidanceDocumentLoader(new ObjectMapper()))));

    @Test
    void goodsNotReceivedMissingDeliveryEvidence() {
        DisputeTriageResponse response = service.triage(
            DisputeTriageTestDataFactory.goodsNotReceivedMissingDeliveryEvidence());

        assertTrue(response.getLikelyMissingEvidence().contains(EvidenceType.PROOF_OF_DELIVERY));
        assertEquals(RecommendedAction.GATHER_DELIVERY_EVIDENCE.getMessage(), response.getNextRecommendedAction());
    }

    @Test
    void goodsNotReceivedCompleteEvidenceUsesReviewCaseReadinessAction() {
        DisputeTriageResponse response = service.triage(
            DisputeTriageTestDataFactory.goodsNotReceivedCompleteEvidence());

        assertTrue(response.getLikelyMissingEvidence().isEmpty());
        assertEquals(REVIEW_CASE_READINESS_MESSAGE, response.getNextRecommendedAction());
    }

    @Test
    void serviceNotProvidedMissingServiceLog() {
        DisputeTriageResponse response = service.triage(
            DisputeTriageTestDataFactory.serviceNotProvidedMissingServiceLog());

        assertTrue(response.getLikelyMissingEvidence().contains(EvidenceType.CANCELLATION_POLICY));
    }

    @Test
    void serviceNotProvidedNarrativeMatchesCancellationUsesCancellationPolicyEvidence() {
        DisputeTriageRequest request = serviceNotProvidedRequest(
            "Customer canceled subscription but was still charged.",
            List.of(EvidenceType.CUSTOMER_COMMUNICATION));

        DisputeTriageResponse response = service.triage(request);

        assertTrue(response.getLikelyMissingEvidence().contains(EvidenceType.CANCELLATION_POLICY));
    }

    @Test
    void serviceNotProvidedNarrativeMatchesDigitalAccessUsesRequestMoreInformation() {
        DisputeTriageRequest request = serviceNotProvidedRequest(
            "Activation failed and customer cannot access the service.",
            List.of(EvidenceType.CUSTOMER_COMMUNICATION));

        DisputeTriageResponse response = service.triage(request);

        assertEquals(RecommendedAction.REQUEST_MORE_INFORMATION.getMessage(), response.getNextRecommendedAction());
    }

    @Test
    void duplicateProcessingMissingRefundRecordUsesGuidanceRecommendation() {
        DisputeTriageResponse response = service.triage(
            DisputeTriageTestDataFactory.duplicateProcessingMissingRefundRecord());

        assertEquals(RecommendedAction.CHECK_FOR_DUPLICATE_CHARGE.getMessage(), response.getNextRecommendedAction());
    }

    @Test
    void duplicateProcessingCompleteEvidenceUsesReviewCaseReadinessAction() {
        DisputeTriageResponse response = service.triage(
            DisputeTriageTestDataFactory.duplicateProcessingCompleteEvidence());

        assertEquals(REVIEW_CASE_READINESS_MESSAGE, response.getNextRecommendedAction());
    }

    @Test
    void invalidDisputeDateBeforeTransactionDateThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.triage(
            DisputeTriageTestDataFactory.invalidDateCase()));
    }

    private DisputeTriageRequest serviceNotProvidedRequest(String narrative, List<EvidenceType> evidenceProvided) {
        DisputeTriageRequest request = new DisputeTriageRequest();
        request.setScheme(CardScheme.VISA);
        request.setReasonCode(DisputeReasonCode.SERVICE_NOT_PROVIDED);
        request.setAmount(new BigDecimal("19.99"));
        request.setTransactionDate(LocalDate.of(2025, 1, 10));
        request.setDisputeDate(LocalDate.of(2025, 1, 12));
        request.setMerchantNarrative(narrative);
        request.setEvidenceProvided(evidenceProvided);
        return request;
    }
}
