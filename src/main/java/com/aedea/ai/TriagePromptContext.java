package com.aedea.ai;

import com.aedea.domain.enums.CardScheme;
import com.aedea.domain.enums.CaveatType;
import com.aedea.domain.enums.DisputeReasonCode;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.domain.enums.guidance.RecommendedAction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class TriagePromptContext {
    private CardScheme scheme;
    private DisputeReasonCode reasonCode;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private LocalDate disputeDate;
    private String merchantNarrative;
    private List<EvidenceType> evidenceProvided;
    private String matchedGuidanceTitle;
    private List<String> supportingGuidanceNotes;
    private List<EvidenceType> missingEvidence;
    private RecommendedAction recommendedAction;
    private List<CaveatType> caveats;
}
