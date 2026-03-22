package com.aedea.guidance;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
public class MockGuidanceDocumentLoader {
    private static final String RESOURCE_PATH = "guidance/mock-guidance-documents.json";

    private final ObjectMapper objectMapper;

    public MockGuidanceDocumentLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<MockGuidanceDocument> loadDocuments() {
        ClassPathResource resource = new ClassPathResource(RESOURCE_PATH);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<List<MockGuidanceDocument>>() {});
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load mock guidance documents", exception);
        }
    }
}
