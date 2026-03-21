package com.aedea.api;

import com.aedea.dto.DisputeTriageRequest;
import com.aedea.dto.DisputeTriageResponse;
import com.aedea.service.DisputeTriageService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class DisputeTriageController {
    private final DisputeTriageService disputeTriageService;

    public DisputeTriageController(DisputeTriageService disputeTriageService) {
        this.disputeTriageService = disputeTriageService;
    }

    @PostMapping("/api/disputes/triage")
    public DisputeTriageResponse triage(@Valid @RequestBody DisputeTriageRequest request) {
        return disputeTriageService.triage(request);
    }
}
