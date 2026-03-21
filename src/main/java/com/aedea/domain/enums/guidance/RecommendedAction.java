package com.aedea.domain.enums.guidance;

public enum RecommendedAction {
    GATHER_DELIVERY_EVIDENCE("Gather delivery evidence."),
    REVIEW_SERVICE_RECORDS("Review service records."),
    CHECK_FOR_DUPLICATE_CHARGE("Check for duplicate charge."),
    REQUEST_MORE_INFORMATION("Request more information."),
    REVIEW_CASE_READINESS("Review available evidence and decide whether the case is ready to contest");

    private final String message;

    RecommendedAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
