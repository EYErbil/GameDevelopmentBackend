package com.dreamgames.backendengineeringcasestudy.entity;

public enum AbGroup {
    A, B;

    public static AbGroup fromString(String value) {
        try {
            return AbGroup.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid value for AbGroup: " + value);
        }
    }
}
