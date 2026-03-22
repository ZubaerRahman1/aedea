package com.aedea.service;

import com.aedea.domain.enums.CardScheme;
import com.aedea.domain.enums.DisputeReasonCode;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.dto.DisputeTriageRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class DisputeTriageTestDataFactory {

    private DisputeTriageTestDataFactory() {
    }

    public static DisputeTriageRequest goodsNotReceivedMissingDeliveryEvidence() {
        DisputeTriageRequest request = baseRequest(DisputeReasonCode.GOODS_NOT_RECEIVED);
        request.setEvidenceProvided(List.of(EvidenceType.CUSTOMER_COMMUNICATION));
        return request;
    }

    public static DisputeTriageRequest goodsNotReceivedCompleteEvidence() {
        DisputeTriageRequest request = baseRequest(DisputeReasonCode.GOODS_NOT_RECEIVED);
        request.setEvidenceProvided(List.of(EvidenceType.PROOF_OF_DELIVERY, EvidenceType.CUSTOMER_COMMUNICATION));
        return request;
    }

    public static DisputeTriageRequest serviceNotProvidedMissingServiceLog() {
        DisputeTriageRequest request = baseRequest(DisputeReasonCode.SERVICE_NOT_PROVIDED);
        request.setEvidenceProvided(List.of(EvidenceType.CUSTOMER_COMMUNICATION));
        return request;
    }

    public static DisputeTriageRequest duplicateProcessingMissingRefundRecord() {
        DisputeTriageRequest request = baseRequest(DisputeReasonCode.DUPLICATE_PROCESSING);
        request.setEvidenceProvided(List.of(EvidenceType.CUSTOMER_COMMUNICATION));
        return request;
    }

    public static DisputeTriageRequest duplicateProcessingCompleteEvidence() {
        DisputeTriageRequest request = baseRequest(DisputeReasonCode.DUPLICATE_PROCESSING);
        request.setEvidenceProvided(List.of(EvidenceType.REFUND_RECORD, EvidenceType.CUSTOMER_COMMUNICATION));
        return request;
    }

    public static DisputeTriageRequest invalidDateCase() {
        DisputeTriageRequest request = baseRequest(DisputeReasonCode.GOODS_NOT_RECEIVED);
        request.setTransactionDate(LocalDate.of(2025, 1, 12));
        request.setDisputeDate(LocalDate.of(2025, 1, 10));
        return request;
    }

    private static DisputeTriageRequest baseRequest(DisputeReasonCode reasonCode) {
        DisputeTriageRequest request = new DisputeTriageRequest();
        request.setScheme(CardScheme.VISA);
        request.setReasonCode(reasonCode);
        request.setAmount(new BigDecimal("42.50"));
        request.setTransactionDate(LocalDate.of(2025, 1, 10));
        request.setDisputeDate(LocalDate.of(2025, 1, 12));
        request.setMerchantNarrative("Customer reported an issue with the transaction.");
        return request;
    }
}
