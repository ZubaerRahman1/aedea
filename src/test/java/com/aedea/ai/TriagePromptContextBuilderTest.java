package com.aedea.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.aedea.domain.TriageAssessment;
import com.aedea.domain.enums.CardScheme;
import com.aedea.domain.enums.CaveatType;
import com.aedea.domain.enums.DisputeReasonCode;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.domain.enums.guidance.RecommendedAction;
import com.aedea.dto.DisputeTriageRequest;
import com.aedea.guidance.MockGuidanceDocument;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

class TriagePromptContextBuilderTest {

    @Test
    void buildsContextFromRequestAndAssessment() {
        DisputeTriageRequest request = getDisputeTriageRequest();

        TriageAssessment assessment = new TriageAssessment();
        assessment.setMatchedGuidanceTitle("Goods Not Received - Delivery Evidence Checklist");
        assessment.setSupportingGuidanceNotes(List.of("Confirm delivery address matches order details."));
        assessment.setMissingEvidence(List.of(EvidenceType.PROOF_OF_DELIVERY));
        assessment.setRecommendedAction(RecommendedAction.GATHER_DELIVERY_EVIDENCE);
        assessment.setCaveatTypes(List.of(CaveatType.MISSING_MERCHANT_NARRATIVE));

        TriagePromptContext context = new TriagePromptContextBuilder().build(request, assessment);

        assertNotNull(context);
        assertEquals(CardScheme.VISA, context.getScheme());
        assertEquals(DisputeReasonCode.GOODS_NOT_RECEIVED, context.getReasonCode());
        assertEquals(List.of(EvidenceType.CUSTOMER_COMMUNICATION), context.getEvidenceProvided());
        assertEquals("Goods Not Received - Delivery Evidence Checklist", context.getMatchedGuidanceTitle());
        assertEquals(RecommendedAction.GATHER_DELIVERY_EVIDENCE, context.getRecommendedAction());
    }

    private static @NonNull DisputeTriageRequest getDisputeTriageRequest() {
        DisputeTriageRequest request = new DisputeTriageRequest();
        request.setScheme(CardScheme.VISA);
        request.setReasonCode(DisputeReasonCode.GOODS_NOT_RECEIVED);
        request.setAmount(new BigDecimal("10.00"));
        request.setTransactionDate(LocalDate.of(2025, 1, 10));
        request.setDisputeDate(LocalDate.of(2025, 1, 12));
        request.setMerchantNarrative("Customer says item never arrived.");
        request.setEvidenceProvided(List.of(EvidenceType.CUSTOMER_COMMUNICATION));
        return request;
    }
}
