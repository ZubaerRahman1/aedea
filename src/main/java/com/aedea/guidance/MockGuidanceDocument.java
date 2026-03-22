package com.aedea.guidance;

import com.aedea.domain.enums.DisputeReasonCode;
import com.aedea.domain.enums.EvidenceType;
import com.aedea.domain.enums.guidance.GuidanceSummaryType;
import com.aedea.domain.enums.guidance.RecommendedAction;
import java.util.List;
import lombok.Data;

@Data
public class MockGuidanceDocument {
    private String documentId;
    private String title;
    private DisputeReasonCode reasonCode;
    private GuidanceSummaryType summaryType;
    private RecommendedAction recommendedAction;
    private List<EvidenceType> expectedEvidence;
    private List<String> operationalNotes;
    private List<String> narrativeKeywords;
}
