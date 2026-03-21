package com.aedea.domain.enums;

public enum CaveatType {
    MISSING_MERCHANT_NARRATIVE("Merchant narrative is missing or blank"),
    LATE_DISPUTE("Dispute was filed long after the transaction date"),
    NO_EVIDENCE_PROVIDED("No supporting evidence was provided"),
    LIMITED_INFORMATION("Recommendation is based on limited information");

    private final String message;

    CaveatType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}