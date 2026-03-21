package com.aedea.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.aedea.domain.enums.CardScheme;
import com.aedea.domain.enums.DisputeReasonCode;
import com.aedea.domain.enums.EvidenceType;
import lombok.Data;

@Data
public class DisputeTriageRequest {
    @NotNull
    private CardScheme scheme;
    @NotNull
    private DisputeReasonCode reasonCode;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotNull
    private LocalDate transactionDate;
    @NotNull
    private LocalDate disputeDate;
    private String merchantNarrative;
    private List<EvidenceType> evidenceProvided;
}
