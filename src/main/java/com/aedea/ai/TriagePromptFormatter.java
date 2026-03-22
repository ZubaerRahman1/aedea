package com.aedea.ai;

import com.aedea.domain.enums.CaveatType;
import com.aedea.domain.enums.EvidenceType;
import java.util.List;
import java.util.stream.Collectors;

public class TriagePromptFormatter {

    public String format(TriagePromptContext context) {
        StringBuilder builder = new StringBuilder();
        builder.append("Case Summary\n");
        builder.append("Scheme: ").append(context.getScheme()).append("\n");
        builder.append("Reason Code: ").append(context.getReasonCode()).append("\n");
        builder.append("Amount: ").append(context.getAmount()).append("\n");
        builder.append("Transaction Date: ").append(context.getTransactionDate()).append("\n");
        builder.append("Dispute Date: ").append(context.getDisputeDate()).append("\n");
        builder.append("Merchant Narrative: ").append(safeText(context.getMerchantNarrative())).append("\n");
        builder.append("Evidence Provided: ").append(joinEvidence(context.getEvidenceProvided())).append("\n");
        builder.append("Missing Evidence: ").append(joinEvidence(context.getMissingEvidence())).append("\n");
        builder.append("Caveats: ").append(joinCaveats(context.getCaveats())).append("\n\n");

        builder.append("Guidance Context\n");
        builder.append("Matched Guidance: ").append(safeText(context.getMatchedGuidanceTitle())).append("\n");
        builder.append("Recommended Action: ").append(context.getRecommendedAction()).append("\n");
        builder.append("Supporting Notes: ").append(joinNotes(context.getSupportingGuidanceNotes())).append("\n");
        return builder.toString();
    }

    private String joinEvidence(List<EvidenceType> evidence) {
        if (evidence == null || evidence.isEmpty()) {
            return "None";
        }
        return evidence.stream()
            .map(Enum::name)
            .collect(Collectors.joining(", "));
    }

    private String joinCaveats(List<CaveatType> caveats) {
        if (caveats == null || caveats.isEmpty()) {
            return "None";
        }
        return caveats.stream()
            .map(Enum::name)
            .collect(Collectors.joining(", "));
    }

    private String joinNotes(List<String> notes) {
        if (notes == null || notes.isEmpty()) {
            return "None";
        }
        return String.join(" | ", notes);
    }

    private String safeText(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }
}
