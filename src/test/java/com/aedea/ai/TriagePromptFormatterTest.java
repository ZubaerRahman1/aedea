package com.aedea.ai;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aedea.domain.enums.CardScheme;
import com.aedea.domain.enums.CaveatType;
import com.aedea.domain.enums.DisputeReasonCode;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.domain.enums.guidance.RecommendedAction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class TriagePromptFormatterTest {

    @Test
    void formatsPromptContextAsReadableText() {
        TriagePromptContext context = new TriagePromptContext();
        context.setScheme(CardScheme.VISA);
        context.setReasonCode(DisputeReasonCode.GOODS_NOT_RECEIVED);
        context.setAmount(new BigDecimal("10.00"));
        context.setTransactionDate(LocalDate.of(2025, 1, 10));
        context.setDisputeDate(LocalDate.of(2025, 1, 12));
        context.setMerchantNarrative("Customer says item never arrived.");
        context.setEvidenceProvided(List.of(EvidenceType.CUSTOMER_COMMUNICATION));
        context.setMissingEvidence(List.of(EvidenceType.PROOF_OF_DELIVERY));
        context.setCaveats(List.of(CaveatType.MISSING_MERCHANT_NARRATIVE));
        context.setMatchedGuidanceTitle("Goods Not Received - Delivery Evidence Checklist");
        context.setSupportingGuidanceNotes(List.of("Confirm delivery address matches order details."));
        context.setRecommendedAction(RecommendedAction.GATHER_DELIVERY_EVIDENCE);

        String output = new TriagePromptFormatter().format(context);

        assertTrue(output.contains("Case Summary"));
        assertTrue(output.contains("Reason Code: GOODS_NOT_RECEIVED"));
        assertTrue(output.contains("Guidance Context"));
        assertTrue(output.contains("Supporting Notes: Confirm delivery address matches order details."));
    }
}
