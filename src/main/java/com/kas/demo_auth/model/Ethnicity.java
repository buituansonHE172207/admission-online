package com.kas.demo_auth.model;

public enum Ethnicity {
    KINH("Kinh"),
    TAY("Tày"),
    THAI("Thái"),
    MONG("Mông"),
    HMONG("H Mông"),
    NUNG("Nùng"),
    DAO("Dao"),
    HOA("Hoa"),
    KHMER("Khơ me"),
    CHAM("Chăm");

    private String ethnicityName;

    private Ethnicity(String ethnicityName) {
        this.ethnicityName = ethnicityName;
    }

    public String getDisplayName() {
        return ethnicityName;
    }
}

