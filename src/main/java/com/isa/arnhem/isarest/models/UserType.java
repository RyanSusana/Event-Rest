package com.isa.arnhem.isarest.models;

public enum UserType {
    MEMBER(50), ADMIN(100), STUDENT(0);

    private final int level;

    UserType(final int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
