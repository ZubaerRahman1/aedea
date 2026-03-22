package com.aedea.guidance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aedea.domain.enums.DisputeReasonCode;
import com.aedea.dto.DisputeTriageRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

class MockGuidanceRetrieverTest {

    @Test
    void returnsEmptyWhenNoGuidanceForReasonCode() {
        MockGuidanceRetriever retriever = retrieverWithDocs(
            List.of(doc("GNR-001", DisputeReasonCode.GOODS_NOT_RECEIVED, List.of("not received"))));

        Optional<MockGuidanceDocument> result = retriever.retrieve(
            request(DisputeReasonCode.SERVICE_NOT_PROVIDED, "service not provided"));

        assertTrue(result.isEmpty());
    }

    @Test
    void returnsOnlyCandidateWhenSingleMatchExists() {
        MockGuidanceRetriever retriever = retrieverWithDocs(
            List.of(doc("SNP-201", DisputeReasonCode.SERVICE_NOT_PROVIDED, List.of("canceled subscription"))));

        Optional<MockGuidanceDocument> result = retriever.retrieve(
            request(DisputeReasonCode.SERVICE_NOT_PROVIDED, "canceled subscription"));

        assertEquals("SNP-201", result.orElseThrow().getDocumentId());
    }

    @Test
    void returnsBestKeywordMatchWhenMultipleCandidatesExist() {
        MockGuidanceRetriever retriever = retrieverWithDocs(List.of(
            doc("SNP-201", DisputeReasonCode.SERVICE_NOT_PROVIDED, List.of("canceled subscription")),
            doc("SNP-202", DisputeReasonCode.SERVICE_NOT_PROVIDED, List.of("activation failed"))
        ));

        Optional<MockGuidanceDocument> result = retriever.retrieve(
            request(DisputeReasonCode.SERVICE_NOT_PROVIDED, "Activation failed and cannot access."));

        assertEquals("SNP-202", result.orElseThrow().getDocumentId());
    }

    @Test
    void fallsBackToFirstCandidateWhenNarrativeIsBlank() {
        MockGuidanceRetriever retriever = retrieverWithDocs(List.of(
            doc("SNP-203", DisputeReasonCode.SERVICE_NOT_PROVIDED, List.of("appointment missed")),
            doc("SNP-202", DisputeReasonCode.SERVICE_NOT_PROVIDED, List.of("activation failed"))
        ));

        Optional<MockGuidanceDocument> result = retriever.retrieve(
            request(DisputeReasonCode.SERVICE_NOT_PROVIDED, " "));

        assertEquals("SNP-203", result.orElseThrow().getDocumentId());
    }

    @Test
    void fallsBackDeterministicallyWhenKeywordScoresAreTied() {
        MockGuidanceRetriever retriever = retrieverWithDocs(List.of(
            doc("SNP-203", DisputeReasonCode.SERVICE_NOT_PROVIDED, List.of("service issue")),
            doc("SNP-201", DisputeReasonCode.SERVICE_NOT_PROVIDED, List.of("service issue"))
        ));

        Optional<MockGuidanceDocument> result = retriever.retrieve(
            request(DisputeReasonCode.SERVICE_NOT_PROVIDED, "service issue reported"));

        assertEquals("SNP-201", result.orElseThrow().getDocumentId());
    }

    private MockGuidanceRetriever retrieverWithDocs(List<MockGuidanceDocument> documents) {
        MockGuidanceDocumentLoader loader = new StubGuidanceDocumentLoader(documents);
        return new MockGuidanceRetriever(new MockGuidanceDocumentCatalog(loader));
    }

    private DisputeTriageRequest request(DisputeReasonCode reasonCode, String narrative) {
        DisputeTriageRequest request = new DisputeTriageRequest();
        request.setReasonCode(reasonCode);
        request.setMerchantNarrative(narrative);
        return request;
    }

    private MockGuidanceDocument doc(String id, DisputeReasonCode reasonCode, List<String> keywords) {
        MockGuidanceDocument document = new MockGuidanceDocument();
        document.setDocumentId(id);
        document.setTitle("Doc " + id);
        document.setReasonCode(reasonCode);
        document.setNarrativeKeywords(keywords);
        return document;
    }

    private static final class StubGuidanceDocumentLoader extends MockGuidanceDocumentLoader {
        private final List<MockGuidanceDocument> documents;

        private StubGuidanceDocumentLoader(List<MockGuidanceDocument> documents) {
            super(new ObjectMapper());
            this.documents = documents;
        }

        @Override
        public List<MockGuidanceDocument> loadDocuments() {
            return documents;
        }
    }
}
