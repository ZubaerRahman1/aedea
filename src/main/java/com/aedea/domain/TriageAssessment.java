package com.aedea.domain;

import com.aedea.domain.enums.CaveatType;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.domain.enums.guidance.GuidanceSummaryType;
import com.aedea.domain.enums.guidance.RecommendedAction;
import java.util.List;
import lombok.Data;

@Data
public class TriageAssessment {
    private String matchedGuidanceTitle;
    private boolean guidanceMatched;
    private String guidanceSource;
    private String actionReason;
    private List<EvidenceType> expectedEvidence;
    private List<String> supportingGuidanceNotes;
    private GuidanceSummaryType summaryType;
    private RecommendedAction recommendedAction;
    private List<EvidenceType> missingEvidence;
    private List<CaveatType> caveatTypes;
    private String guidanceDocumentId;
}
