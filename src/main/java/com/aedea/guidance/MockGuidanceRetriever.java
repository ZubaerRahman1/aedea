package com.aedea.guidance;

import com.aedea.dto.DisputeTriageRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MockGuidanceRetriever implements GuidanceRetriever {
    private final MockGuidanceDocumentCatalog catalog;

    public MockGuidanceRetriever(MockGuidanceDocumentCatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public Optional<MockGuidanceDocument> retrieve(DisputeTriageRequest request) {
        List<MockGuidanceDocument> candidates = catalog.findAllByReasonCode(request.getReasonCode());
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        if (candidates.size() == 1) {
            return Optional.of(candidates.get(0));
        }
        return selectBestMatch(candidates, request.getMerchantNarrative());
    }

    private Optional<MockGuidanceDocument> selectBestMatch(
        List<MockGuidanceDocument> documents,
        String merchantNarrative
    ) {
        if (merchantNarrative == null || merchantNarrative.isBlank()) {
            return Optional.of(documents.get(0));
        }

        String normalizedNarrative = merchantNarrative.toLowerCase();
        MockGuidanceDocument best = documents.get(0);
        int bestScore = overlapScore(best, normalizedNarrative);
        String bestId = safeId(best);

        for (int i = 1; i < documents.size(); i++) {
            MockGuidanceDocument candidate = documents.get(i);
            int candidateScore = overlapScore(candidate, normalizedNarrative);
            String candidateId = safeId(candidate);

            if (candidateScore > bestScore
                // Tie-breaker: prefer lexicographically smaller documentId for deterministic selection.
                || (candidateScore == bestScore && candidateId.compareTo(bestId) < 0)) {
                best = candidate;
                bestScore = candidateScore;
                bestId = candidateId;
            }
        }

        return Optional.of(best);
    }

    private int overlapScore(MockGuidanceDocument document, String normalizedNarrative) {
        List<String> keywords = document.getNarrativeKeywords();
        if (keywords == null || keywords.isEmpty()) {
            return 0;
        }

        int score = 0;
        for (String keyword : keywords) {
            if (keyword != null && !keyword.isBlank()
                && normalizedNarrative.contains(keyword.toLowerCase())) {
                score++;
            }
        }
        return score;
    }

    private String safeId(MockGuidanceDocument document) {
        return document.getDocumentId() == null ? "" : document.getDocumentId();
    }
}
