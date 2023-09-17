package com.kas.demo_auth.model;

public enum AdmissionStatus {
    PENDING("Đang chờ"),
    APPROVED("Đã được duyệt"),
    REJECTED("Bị từ chối");

    private final String displayName;

    AdmissionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
