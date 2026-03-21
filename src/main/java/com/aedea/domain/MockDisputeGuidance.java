package com.aedea.domain;

import com.aedea.domain.enums.DisputeReasonCode;
import com.aedea.domain.enums.EvidenceType;

import java.util.List;

import com.aedea.domain.enums.guidance.GuidanceSummaryType;
import com.aedea.domain.enums.guidance.RecommendedAction;
import lombok.Data;

@Data
public class MockDisputeGuidance {
    private DisputeReasonCode reasonCode;
    private List<EvidenceType> recommendedEvidence;
    private RecommendedAction recommendedAction;
    private GuidanceSummaryType guidanceSummary;
}
