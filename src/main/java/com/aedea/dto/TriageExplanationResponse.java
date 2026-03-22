package com.aedea.dto;

import lombok.Data;

@Data
public class TriageExplanationResponse {
    private boolean guidanceMatched;
    private String matchedGuidanceTitle;
    private String guidanceSource;
    private String actionReason;
}
