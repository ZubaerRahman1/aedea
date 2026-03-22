package com.aedea.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aedea.domain.enums.guidance.RecommendedAction;
import com.aedea.dto.DisputeTriageResponse;
import com.aedea.dto.TriageAiDraftResponse;
import com.aedea.dto.TriageExplanationResponse;
import com.aedea.service.DisputeTriageService;
import com.aedea.service.TriageAiDraftService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DisputeTriageController.class)
@WithMockUser
class DisputeTriageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DisputeTriageService disputeTriageService;

    @MockitoBean
    private TriageAiDraftService triageAiDraftService;

    @Test
    void validRequestReturnsOk() throws Exception {
        DisputeTriageResponse response = new DisputeTriageResponse();
        response.setSummary("ok");
        response.setNextRecommendedAction(RecommendedAction.REVIEW_CASE_READINESS.getMessage());
        response.setTriageExplanation(new TriageExplanationResponse());
        response.setSupportingGuidanceNotes(java.util.List.of("Note 1"));
        when(disputeTriageService.triage(any())).thenReturn(response);

        mockMvc.perform(post("/api/disputes/triage")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson("VISA", "2025-01-10", "2025-01-12")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.summary").exists())
            .andExpect(jsonPath("$.triageExplanation").exists())
            .andExpect(jsonPath("$.supportingGuidanceNotes").isArray());
    }

    @Test
    void missingRequiredFieldReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/disputes/triage")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson(null, "2025-01-10", "2025-01-12")))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void invalidDateOrderReturnsBadRequest() throws Exception {
        when(disputeTriageService.triage(any()))
            .thenThrow(new IllegalArgumentException("disputeDate must be on or after transactionDate"));

        mockMvc.perform(post("/api/disputes/triage")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson("VISA", "2025-01-12", "2025-01-10")))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void promptPreviewReturnsOkWithPromptField() throws Exception {
        when(disputeTriageService.buildPromptPreview(any())).thenReturn("Prompt preview");

        mockMvc.perform(post("/api/disputes/triage/prompt-preview")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson("VISA", "2025-01-10", "2025-01-12")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.prompt").exists());
    }

    @Test
    void aiDraftReturnsOkWithDraftField() throws Exception {
        when(triageAiDraftService.generateDraft(any())).thenReturn("Draft response preview");

        mockMvc.perform(post("/api/disputes/triage/ai-draft")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson("VISA", "2025-01-10", "2025-01-12")))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.draft").exists());
    }

    @Test
    void aiDraftMissingRequiredFieldReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/disputes/triage/ai-draft")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson(null, "2025-01-10", "2025-01-12")))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").exists());
    }

    private String requestJson(String scheme, String transactionDate, String disputeDate) {
        String schemeLine = scheme == null ? "" : "  \"scheme\": \"" + scheme + "\",\n";
        return """
            {
            %s  "reasonCode": "GOODS_NOT_RECEIVED",
              "amount": 10.50,
              "transactionDate": "%s",
              "disputeDate": "%s",
              "merchantNarrative": "Customer reported non-receipt",
              "evidenceProvided": ["PROOF_OF_DELIVERY"]
            }
            """.formatted(schemeLine, transactionDate, disputeDate);
    }
}
