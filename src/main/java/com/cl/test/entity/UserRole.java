package com.cl.test.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    TUTOR,
    STUDENT;

    @JsonValue
    public String getValue() {
        return name();
    }

    @Override
    public String toString() {
        return name();
    }
}