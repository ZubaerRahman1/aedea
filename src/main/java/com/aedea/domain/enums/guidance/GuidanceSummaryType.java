package com.aedea.domain.enums.guidance;

public enum GuidanceSummaryType {
    NON_RECEIPT_CLAIM("Non-receipt claim."),
    SERVICE_FULFILLMENT_ISSUE("Service fulfillment issue."),
    POSSIBLE_DUPLICATE_TRANSACTION("Possible duplicate transaction."),
    GENERAL_REVIEW_REQUIRED("General review required.");

    private final String message;

    GuidanceSummaryType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
