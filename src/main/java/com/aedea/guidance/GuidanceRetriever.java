package com.aedea.guidance;

import com.aedea.dto.DisputeTriageRequest;
import java.util.Optional;

public interface GuidanceRetriever {
    Optional<MockGuidanceDocument> retrieve(DisputeTriageRequest request);
}
