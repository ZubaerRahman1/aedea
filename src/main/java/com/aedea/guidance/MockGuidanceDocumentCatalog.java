package com.aedea.guidance;

import com.aedea.domain.enums.DisputeReasonCode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MockGuidanceDocumentCatalog {
    private final List<MockGuidanceDocument> documents;

    public MockGuidanceDocumentCatalog(MockGuidanceDocumentLoader loader) {
        this.documents = loader.loadDocuments();
    }

    public Optional<MockGuidanceDocument> findByReasonCode(DisputeReasonCode reasonCode) {
        return findAllByReasonCode(reasonCode).stream().findFirst();
    }

    public List<MockGuidanceDocument> findAllByReasonCode(DisputeReasonCode reasonCode) {
        return documents.stream()
            .filter(document -> document.getReasonCode() == reasonCode)
            .collect(Collectors.toList());
    }
}
