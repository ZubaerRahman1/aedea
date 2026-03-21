package com.aedea.guidance;

import com.aedea.domain.MockDisputeGuidance;
import com.aedea.domain.enums.DisputeReasonCode;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.domain.enums.guidance.GuidanceSummaryType;
import com.aedea.domain.enums.guidance.RecommendedAction;
import java.util.List;
import java.util.Map;

public class MockGuidanceCatalog {
    private final Map<DisputeReasonCode, MockDisputeGuidance> guidanceByReasonCode;

    public MockGuidanceCatalog() {
        this.guidanceByReasonCode = Map.of(
            DisputeReasonCode.GOODS_NOT_RECEIVED, guidanceForGoodsNotReceived(),
            DisputeReasonCode.SERVICE_NOT_PROVIDED, guidanceForServiceNotProvided(),
            DisputeReasonCode.DUPLICATE_PROCESSING, guidanceForDuplicateProcessing()
        );
    }

    public MockDisputeGuidance getGuidance(DisputeReasonCode reasonCode) {
        return guidanceByReasonCode.get(reasonCode);
    }

    private MockDisputeGuidance guidanceForGoodsNotReceived() {
        MockDisputeGuidance guidance = new MockDisputeGuidance();
        guidance.setReasonCode(DisputeReasonCode.GOODS_NOT_RECEIVED);
        guidance.setRecommendedEvidence(List.of(EvidenceType.PROOF_OF_DELIVERY, EvidenceType.CUSTOMER_COMMUNICATION));
        guidance.setRecommendedAction(RecommendedAction.GATHER_DELIVERY_EVIDENCE);
        guidance.setGuidanceSummary(GuidanceSummaryType.NON_RECEIPT_CLAIM);
        return guidance;
    }

    private MockDisputeGuidance guidanceForServiceNotProvided() {
        MockDisputeGuidance guidance = new MockDisputeGuidance();
        guidance.setReasonCode(DisputeReasonCode.SERVICE_NOT_PROVIDED);
        guidance.setRecommendedEvidence(List.of(EvidenceType.SERVICE_LOG, EvidenceType.CUSTOMER_COMMUNICATION));
        guidance.setRecommendedAction(RecommendedAction.REVIEW_SERVICE_RECORDS);
        guidance.setGuidanceSummary(GuidanceSummaryType.SERVICE_FULFILLMENT_ISSUE);
        return guidance;
    }

    private MockDisputeGuidance guidanceForDuplicateProcessing() {
        MockDisputeGuidance guidance = new MockDisputeGuidance();
        guidance.setReasonCode(DisputeReasonCode.DUPLICATE_PROCESSING);
        guidance.setRecommendedEvidence(List.of(EvidenceType.REFUND_RECORD, EvidenceType.CUSTOMER_COMMUNICATION));
        guidance.setRecommendedAction(RecommendedAction.CHECK_FOR_DUPLICATE_CHARGE);
        guidance.setGuidanceSummary(GuidanceSummaryType.POSSIBLE_DUPLICATE_TRANSACTION);
        return guidance;
    }
}
