package com.aedea.dto;

import java.util.List;

import com.aedea.domain.enums.CaveatType;
import com.aedea.domain.enums.EvidenceType;
import lombok.Data;

@Data
public class DisputeTriageResponse {
    private String summary;
    private List<EvidenceType> likelyMissingEvidence;
    private String nextRecommendedAction;
    private List<CaveatType> caveats;
}
